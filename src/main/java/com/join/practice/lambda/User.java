package com.join.practice.lambda;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String userName;
    private String sex;
    private Integer age;

    public User() {
        this.id = id;
    }

    public User(int id, String userName, String sex, int age) {
        this.id = id;
        this.userName = userName;
        this.sex = sex;
        this.age = age;
    }

}
