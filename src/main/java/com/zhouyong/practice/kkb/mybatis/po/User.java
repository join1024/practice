package com.zhouyong.practice.kkb.mybatis.po;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String username;
    private String nickname;
    private Date birthday;
    private String sex;
    private String address;
}