package com.zhouyong.practice.validator.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author join
 */
@Data
public class PersonBean {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Range(min = 0, max = 100, message = "年龄必须在{min}和{max}之间")
    private Integer age;

    @NotNull(message = "是否已婚不能为空")
    private Boolean isMarried;

    @NotNull(message = "是否有小孩不能为空")
    private Boolean hasChild;

    @NotNull(message = "小孩个数不能为空")
    private Integer childCount;

    @NotNull(message = "是否单身不能为空")
    private Boolean isSingle;

}