package com.zhouyong.practice.validator.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author join
 */
@Documented
//绑定校验器
@Constraint(validatedBy = {OrderPriceValidator.class})
//可以发现没有 ElementType.TYPE 该注解也能用到类上面，这是因为ElementType.TYPE_USE包含ElementType.TYPE
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OrderPrice.List.class)
public @interface OrderPrice {

    Class<?>[] groups() default {};

    String message() default "订单价格不符合校验规则";

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        OrderPrice[] value();
    }
}
