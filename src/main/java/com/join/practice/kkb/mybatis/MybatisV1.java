package com.join.practice.kkb.mybatis;

import com.join.practice.kkb.mybatis.po.User;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * V1版本：jdb链接和sql从配置文件读取
 */
public class MybatisV1 {

    private Properties properties;

    @Test
    public void test() throws IOException, ClassNotFoundException {
        //加载配置
        loadProperties("/jdbc.properties");

        //查询1
        List<User> list1=selectList("queryUserById",1);
        System.out.println("list1="+list1);

        //查询2
        List<User> list2=selectList("queryUserByName","李四");
        System.out.println("list2="+list2);

        //查询3
        Map<String,Object> param=new HashMap<>();
        param.put("username","小红");
        param.put("nickname","xiaohong");
        List<User> list3=selectList("queryUserByParams",param);
        System.out.println("list3="+list3);

    }

    //加载配置文件
    private void loadProperties(String propertiesSource) throws IOException {
        InputStream inputStream=this.getClass().getResourceAsStream(propertiesSource);
        properties=new Properties();
        properties.load(inputStream);
    }

    private <T> List<T> selectList(String statementId, Object param){
        String dbDriver=properties.getProperty("db.driver");
        String dbUrl=properties.getProperty("db.url");
        String dbUser=properties.getProperty("db.username");
        String dbPwd=properties.getProperty("db.password");
        String sql=properties.getProperty("db.sql." + statementId);
        String columnNames = properties.getProperty("db.sql." + statementId + ".columnnames");

        Connection connection=null;
        ResultSet resultSet=null;
        try{
            /*1. 初始化连接池*/
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(dbDriver);
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(dbUser);
            dataSource.setPassword(dbPwd);

            /*2. 通过连接池获取连接*/
            connection = dataSource.getConnection();

            /*3.创建statement*/
            PreparedStatement statement=connection.prepareStatement(sql);

            if(param!=null){
                if (isBasicType(param)){
                    //如果是基本类型
                    statement.setObject(1,param);

                }else if(param instanceof Map){
                    //如果是Map
                    Map map=(Map)param;
                    if(columnNames!=null && columnNames.trim()!=""){
                        String[] colNames=columnNames.split(",");
                        for (int i = 0; i < colNames.length; i++) {
                            Object p=map.get(colNames[i].trim());
                            statement.setObject(i+1,p);
                        }
                    }

                }else{
                    throw new RuntimeException("不支持的类型："+param.getClass().getName());
                }
            }

            /*4.执行statement*/
            resultSet=statement.executeQuery();

            List<T> list=new ArrayList();
            String resultType=properties.getProperty("db.sql." + statementId + ".resulttype");
            Class clazz=Class.forName(resultType);

            /*5.处理结果集*/
            while(resultSet.next()){
                T t=(T)clazz.newInstance();
                list.add(t);
                for (Field field : clazz.getDeclaredFields()) {
                    Object value=resultSet.getObject(field.getName());
                    field.setAccessible(true);
                    field.set(t,value);
                }
            }

            return list;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }finally {
            try{
                if(resultSet!=null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(),e);
            }finally {
                if(connection!=null){
                    try {
                        //这里关闭连接实际上不是真正关闭，而是将连接释放到连接池中，方便复用。
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage(),e);
                    }
                }
            }
        }
    }

    //基本类型判断
    private boolean isBasicType(Object parameterObj) {
        if (parameterObj instanceof Byte) {
            return true;
        } else if (parameterObj instanceof String) {
            return true;
        }else if (parameterObj instanceof Short) {
            return true;
        } else if (parameterObj instanceof Integer) {
            return true;
        } else if (parameterObj instanceof Long) {
            return true;
        } else if (parameterObj instanceof Float) {
            return true;
        } else if (parameterObj instanceof Double) {
            return true;
        }else if (parameterObj instanceof Boolean) {
            return true;
        }
        return false;
    }


}
