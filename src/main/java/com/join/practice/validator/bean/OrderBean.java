package com.join.practice.validator.bean;

import com.join.practice.validator.validate.OrderPrice;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author join
 */
@Data
//类上面用到自定义的校验注解
@OrderPrice
public class OrderBean {

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @NotNull(message = "商品价格不能为空")
    private Double goodsPrice;

    @NotNull(message = "商品数量不能为空")
    private Integer goodsCount;

    @NotNull(message = "订单价格不能为空")
    private Double price;

    @NotBlank(message = "订单备注不能为空")
    private String remark;

}