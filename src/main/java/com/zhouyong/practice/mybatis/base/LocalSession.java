package com.zhouyong.practice.mybatis.base;

import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhouyong
 * @date 2023/7/23 9:52 上午
 */
public class LocalSession {

    /** mybatis 的 session */
    private SqlSession session;

    /** sql 的 connection */
    private Connection connection;

    /** 是否提交事物,单元测试一般不需要提交事物（直接回滚） */
    private boolean isCommit;

    public LocalSession(SqlSession session, Connection connection, boolean isCommit) throws SQLException {
        this.isCommit = isCommit;
        this.session = session;
        this.connection = connection;
    }

    /**
     * 获取mapper对象
     * @param mapperClass
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> mapperClass){
        return session.getMapper(mapperClass);
    }

    /**
     * 关闭session
     * @throws SQLException
     */
    public void close(){
        try{
            if(isCommit){
                connection.commit();
            }else{
                connection.rollback();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                session.close();
            }catch (Exception e) {
                e.printStackTrace();
            }/*finally {
                try {
                    if(!connection.isClosed()){
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

}
