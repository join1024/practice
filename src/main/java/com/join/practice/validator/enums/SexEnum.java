package com.join.practice.validator.enums;

/**
 * @author join
 * @date 2022/4/9 3:15 下午
 */
public enum SexEnum {
    F("女"),
    M("男");

    String desc;
    SexEnum(String desc){
        this.desc=desc;
    }

}
