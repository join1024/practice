package com.join.practice.validator.bean;

import com.join.practice.validator.group.Group;
import com.join.practice.validator.provider.PeopleGroupSequenceProvider;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author join
 */
@Data
public class PeopleBean {

    @NotBlank(message = "姓名不能为空",groups = {Group.UnMarried.class, Group.Married.class})
    private String name;

    @NotNull(message = "年龄不能为空",groups = {Group.UnMarried.class, Group.Married.class})
    @Range(min = 0, max = 100, message = "年龄必须在{min}和{max}之间",groups = {Group.UnMarried.class, Group.Married.class})
    private Integer age;

    @NotNull(message = "是否已婚不能为空")
    private Boolean isMarried;

    @NotNull(message = "配偶姓名",groups = {Group.Married.class})
    private String spouseName;

    @NotNull(message = "是否有小孩不能为空",groups = {Group.Married.class})
    private Boolean hasChild;

    @NotNull(message = "是否单身不能为空",groups = {Group.UnMarried.class})
    private Boolean isSingle;


}