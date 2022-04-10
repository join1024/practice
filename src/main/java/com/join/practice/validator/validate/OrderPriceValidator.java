package com.join.practice.validator.validate;

import com.join.practice.validator.bean.OrderBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;

/**
 * @author join
 * @date 2022/4/9 3:32 下午
 */
public class OrderPriceValidator implements ConstraintValidator<OrderPrice, OrderBean> {

    @Override
    public void initialize(OrderPrice constraintAnnotation) {

    }

    @Override
    public boolean isValid(OrderBean order, ConstraintValidatorContext constraintValidatorContext) {
        if(order==null){
            return true;
        }
        return order.getPrice()==order.getGoodsPrice()*order.getGoodsCount();
    }

}
