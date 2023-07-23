package com.zhouyong.practice.validator.bean;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author join
 * @date 2021/5/24 下午10:07
 */
@Data
public class OrgBean {
    @NotNull
    private Integer id;
    @Valid
    @NotNull(message = "employee不能为空")
    private EmployeeBean Employee;

}
