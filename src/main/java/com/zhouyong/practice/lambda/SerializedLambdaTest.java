package com.zhouyong.practice.lambda;

import org.springframework.beans.BeanUtils;

public class SerializedLambdaTest {
    public static void main(String[] args) {
        User user1=new User(1,"张三","男",18);
        User user2=new User();
        User user3=new User();

        //通过Spring的 BeanUtils 工具类拷贝对象，并且忽略“sex"和”age"两个字段
        BeanUtils.copyProperties(user1,user2,"sex","age");

        //通过自定义的 MyBeanUtils 工具类拷贝对象，并且忽略“sex"和”age"两个字段
        MyBeanUtils.copyProperties(user1,user3,User::getSex,User::getAge);

        System.out.println("user1 = "+user1);
        System.out.println("user2 = "+user2);
        System.out.println("user3 = "+user3);


        //MyBeanUtils.getPropertyName((User u)->u.getSex());
        MyBeanUtils.getPropertyName(User::getSex);
    }
}

