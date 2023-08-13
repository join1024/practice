package com.zhouyong.practice.mybatis.base;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * mybatis单元测试基类
 * @author zhouyong
 * @date 2023/7/23 9:45 上午
 */
public class MybatisBaseTest {

    private final static String configLocation = "mybatis/mybatis-config-test.xml";

    private static ThreadLocal<LocalSession> sessionThreadLocal;

    private static List<LocalSession> sessionPool;

    private static SqlSessionFactory sqlSessionFactory;


    /**
     * 单元类测试启动前的初始化动作
     * 初始化数据库session等相关信息
     */
    @BeforeClass
    public final static void init() throws IOException {

        //多个单元测试类批量执行时，init方法会重复执行，因此做空判断避免重复执行
        if(sqlSessionFactory!=null){
            return ;
        }

        //解析mybatis全局配置文件
        Configuration configuration = parseConfiguration();
        //解析mapper配置
        parseMapperXmlResource(configuration);
        //创建SqlSessionFactory
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        //用于存储所有的session
        sessionPool = new ArrayList<>();
        //LocalSession的线程本地变量
        sessionThreadLocal = new ThreadLocal<>();
        //保底操作，确保异常退出时关闭所有数据库连接
        Runtime.getRuntime().addShutdownHook(new Thread(()->closeAllSession()));
    }

    /**
     * 启动session并且注入mapper对象
     * 每一个单元测试方法启动之前会自动执行该方法
     * 如果子类也有@Before方法，父类的@Before方法先于子类执行
     */
    @Before
    public final void openSessionAndInjectMapper(){
        LocalSession localSession = createLocalSession();
        sessionThreadLocal.set(localSession);
        sessionPool.add(localSession);
        injectMapper();
    }

    /**
     * mapper代理对象注入
     */
    private void injectMapper(){
        Class<? extends MybatisBaseTest> testClass = this.getClass();
        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if(field.getAnnotation(javax.annotation.Resource.class)!=null){
                boolean accessible = field.isAccessible();
                try {
                    if(!accessible){
                        field.setAccessible(true);
                    }
                    Object mapperObj = sessionThreadLocal.get().getMapper(field.getType());
                    field.set(this,mapperObj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("mapper对象注入失败:"+field.getName(),e);
                }finally {
                    field.setAccessible(accessible);
                }
            }
        }

    }

    /**
     * 关闭session
     * 每一个单元测试执行完之后都会自动执行该方法
     * 如果子类也有@After方法，则子类的@After方法先于父类执行（于@Before方法相反）
     */
    @After
    public final void closeSession(){
        LocalSession localSession = sessionThreadLocal.get();
        if(localSession!=null){
            localSession.close();
            sessionPool.remove(localSession);
            sessionThreadLocal.remove();
        }
    }

    /**
     * 保底操作，异常退出时关闭所有session
     */
    public final static void closeAllSession(){
        if(sessionPool!=null){
            for (LocalSession localSession : sessionPool) {
                localSession.close();
            }
            sessionPool.clear();
            sessionPool = null;
        }
        sessionThreadLocal = null;
    }

    /**
     * 解析mybatis全局配置文件
     * @throws IOException
     */
    private final static Configuration parseConfiguration() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(configLocation);
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
        Configuration configuration = parser.parse();

        //驼峰命名自动转换
        configuration.setMapUnderscoreToCamelCase(true);

        Properties properties = configuration.getVariables();
        //如果密码有加密，则此处可以进行解密
        //String pwd = properties.getProperty("jdbcPassword");
        //((PooledDataSource)configuration.getEnvironment().getDataSource()).setPassword("解密后的密码");

        return configuration;
    }

    /**
     * 解析mapper配置文件
     * @throws IOException
     */
    private final static void parseMapperXmlResource(Configuration configuration) throws IOException {
        String[] mapperLocations = configuration.getVariables().getProperty("mapperLocations").split(",");
        //借助spring的FileSystemXmlApplicationContext工具类，根据配置匹配解析出所有路径
        FileSystemXmlApplicationContext xmlContext = new FileSystemXmlApplicationContext();

        for (String mapperLocation : mapperLocations) {
            Resource[] mapperResources = xmlContext.getResources(mapperLocation);
            for (Resource mapperRes : mapperResources) {
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperRes.getInputStream(),
                        configuration,
                        mapperRes.toString(),
                        configuration.getSqlFragments());
                xmlMapperBuilder.parse();
            }

        }
    }

    /**
     * 创建自定义的LocalSession
     * @return
     */
    private final LocalSession createLocalSession(){
        try{
            String isCommitStr = sqlSessionFactory.getConfiguration().getVariables().getProperty("isCommit");
            boolean isCommit = StringUtils.isEmpty(isCommitStr) ? false : Boolean.parseBoolean(isCommitStr);

            SqlSession sqlSession = sqlSessionFactory.openSession(false);
            Connection connection = sqlSession.getConnection();
            connection.setAutoCommit(false);

            return new LocalSession(sqlSession, connection, isCommit);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
