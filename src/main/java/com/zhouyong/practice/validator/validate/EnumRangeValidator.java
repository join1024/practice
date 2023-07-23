package com.zhouyong.practice.validator.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author join
 * @date 2022/4/9 3:32 下午
 */
public class EnumRangeValidator implements ConstraintValidator<EnumRange,String> {

    private Set<String> enumNames;
    private String enumNameStr;

    @Override
    public void initialize(EnumRange constraintAnnotation) {
        Class<? extends Enum> enumType=constraintAnnotation.enumType();
        if(enumType==null){
            throw new IllegalArgumentException("EnumRange.enumType 不能为空");
        }
        try {
            //初始化:将枚举值放到Set中，用于校验
            Method valuesMethod = enumType.getMethod("values");
            Enum[] enums = (Enum[]) valuesMethod.invoke(null);
            enumNames = Stream.of(enums).map(Enum::name).collect(Collectors.toSet());
            enumNameStr = enumNames.stream().collect(Collectors.joining(","));
        } catch (Exception e) {
            throw new RuntimeException("EnumRangeValidator 初始化异常",e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value==null){
            return true;
        }

        boolean result = enumNames.contains(value);

        if(!result){
            //拿到枚举中的message，并替换变量，这个变量是我自己约定的，
            //你在使用注解的message中有花括号，这里会被替换为用逗号隔开展示的枚举值列表
            String message = constraintValidatorContext
                    .getDefaultConstraintMessageTemplate()
                    .replace("{}",enumNameStr);

            //禁用默认值，否则会有两条message
            constraintValidatorContext.disableDefaultConstraintViolation();

            //添加新的message
            constraintValidatorContext.
                    buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();

        }

        return result;
    }
}
