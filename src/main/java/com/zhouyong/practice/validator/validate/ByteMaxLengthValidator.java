package com.zhouyong.practice.validator.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;

/**
 * @author join
 * @date 2022/4/9 3:32 下午
 */
public class ByteMaxLengthValidator implements ConstraintValidator<ByteMaxLength,String> {

    private int max;
    private Charset charset;

    @Override
    public void initialize(ByteMaxLength constraintAnnotation) {
        max=constraintAnnotation.max();
        charset=Charset.forName(constraintAnnotation.charset());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value==null){
            return true;
        }

        int byteLength = value.getBytes(charset).length;
        //System.out.println("byteLength="+byteLength);

        boolean result = byteLength<=max;

        if(!result){
            //这里随便用一个汉字取巧获取每个中文字符占用该字符集的字节数
            int chBytes = "中".getBytes(charset).length;
            System.out.println("chBytes="+chBytes);
            //计算出最大中文字数
            int chMax = max/chBytes;

            //拿到枚举中的message，并替换变量，这个变量是我自己约定的，
            //约定了两个绑定变量：chMax 和 enMax
            String message = constraintValidatorContext
                    .getDefaultConstraintMessageTemplate()
                    .replace("{chMax}",String.valueOf(chMax))
                    .replace("{enMax}",String.valueOf(max));

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
