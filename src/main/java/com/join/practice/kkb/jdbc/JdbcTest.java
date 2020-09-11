package com.join.practice.kkb.jdbc;

import java.sql.*;

public class JdbcTest {

    public static void main(String[] args) throws Exception {

        Connection connection=null;
        ResultSet resultSet=null;
        try {
            //1. 加载驱动
            Class.forName("com.mysql.jdbc.Driver");

            //2. 创建连接
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/newpy?useSSL=false","root","123456");

            //3. 构建statement
            //该Statement不支持参数占位符，有发生sql注入的危险
            Statement statement=connection.createStatement();

            /*String sql="select * from user_info";
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,"join");*/

            //4. 执行statement
            resultSet=statement.executeQuery("select * from user_info where nickname='join'");

            //5. 结果解析
            System.out.println("ID \t 昵称");
            while(resultSet.next()){
                System.out.println(resultSet.getInt("id")+" \t "+resultSet.getString("nickname"));
            }

        }finally {
            //6. 关闭连接
            try{
                if(resultSet!=null){
                    resultSet.close();
                }
            }finally {
                if(connection!=null){
                    connection.close();;
                }
            }

        }

    }

}
