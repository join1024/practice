package com.zhouyong.practice.validator.bean;

import com.zhouyong.practice.validator.enums.SexEnum;
import com.zhouyong.practice.validator.validate.ByteMaxLength;
import com.zhouyong.practice.validator.validate.EnumRange;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @author join
 */
@Data
public class Person2Bean {

    /**
     * message里面用到了前面约定的两个变量：chMax和enMax,
     * 至于${validatedValue}是框架内置的变量，用于获取当前被校验对象的值
     */
    @ByteMaxLength(max=4,charset = "GBK"
            , message = "姓名【${validatedValue}】全中文字符不能超过{chMax}个字，全英文字符不能超过{enMax}个字母")
    private String name;

    /**
     * 该注解可以用于泛型参数：List<String> ，
     * 这样可以校验List中每一个String元素的字节数是否符合要求
     */
    private List<@ByteMaxLength(max=4,charset = "GBK",
                message = "朋友姓名【${validatedValue}】的字节数不能超过{max}")
                String> friendNames;

    @Range(min = 0, max = 100, message = "年龄必须在{min}和{max}之间")
    private Integer age;

    @EnumRange(enumType = SexEnum.class, message = "性别只能是如下值：{}")
    private String sex;

}