package com.join.practice.jdbc;

import java.sql.*;

public class JdbcTest2 {

    /**
     * 通过 try-with-resource 方式省去了关闭连接的代码，实现了AutoCloseable接口的类都可以通过使用 try-with-resource 方式
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String jdbcDriver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/newpy?useSSL=false";
        String userName="root";
        String userPwd="123456";

        //1. 加载驱动
        Class.forName(jdbcDriver);

        //2. 创建连接
        try (Connection connection=DriverManager.getConnection(url,userName,userPwd)){

            //3. 构建statement
            String sql="select * from user_info where nickname=?";
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,"join");

            //4. 执行statement
            try(ResultSet resultSet=statement.executeQuery("select * from user_info where nickname='join'")){
                //5. 结果解析
                System.out.println("ID \t 昵称");
                while(resultSet.next()){
                    System.out.println(resultSet.getInt("id")+" \t "+resultSet.getString("nickname"));
                }
            }

        }

    }

}
