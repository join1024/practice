package com.zhouyong.practice.validator.validate;

import com.zhouyong.practice.validator.bean.OrderBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
