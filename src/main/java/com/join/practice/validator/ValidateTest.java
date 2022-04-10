package com.join.practice.validator;

import com.join.practice.validator.bean.*;
import com.join.practice.validator.group.Group;
import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author join
 * @date 2021/5/24 下午9:48
 */
public class ValidateTest {
    /**
     * 初始化一个校验器工厂
     */
    private static ValidatorFactory validatorFactory = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            //校验失败是否立即返回： true-遇到一个错误立即返回不在往下校验，false-校验完所有字段才返回
            .failFast(false)
            .buildValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    /**
     * 简单对象校验
     */
    @Test
    public void testSimple() {
        SimpleBean s=new SimpleBean();
        s.setAge(5);
        s.setName(" ");
        s.setEmail("email");

        Set<ConstraintViolation<SimpleBean>> result=validator.validate(s);

        System.out.println("遍历输出错误信息:");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));
    }

    /**
     * 嵌套的对象校验
     */
    @Test
    public void testNested() {
        PersonBean p=new PersonBean();
        p.setAge(30);
        p.setName("zhangsan");
        //p.setIsMarried(true);

        PersonBean p2=new PersonBean();
        p2.setAge(30);
        //p2.setName("zhangsan2");
        p2.setIsMarried(false);
        //p2.setHasChild(true);

        OrgBean org=new OrgBean();
        //org.setId(1);

        List<PersonBean> list=new ArrayList<>();
        list.add(p);
        list.add(p2);
        //
        list.add(null);

        EmployeeBean e=new EmployeeBean();
        e.setPeople(list);
        org.setEmployee(e);

        Set<ConstraintViolation<OrgBean>> result=validator.validate(org);

        System.out.println("遍历输出错误信息：");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));

    }

    /**
     * 分组校验
     */
    @Test
    public void testGroup() {
        PeopleBean p=new PeopleBean();
        p.setAge(30);
        p.setName(" ");
        p.setIsMarried(true);

        Set<ConstraintViolation<PeopleBean>> result;
        if(p.getIsMarried()){
            //如果已婚，则按照已婚的分组字段
            result=validator.validate(p, Group.Married.class);
        }else{
            //如果未婚，则只校验未婚的分组字段
            result=validator.validate(p, Group.UnMarried.class);
        }

        System.out.println("遍历输出错误信息：");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));
    }

    /**
     * 动态分组校验
     * 通过 @GroupSequenceProvider(PeopleGroupSequenceProvider.class) 动态设置分组
     */
    @Test
    public void testGroupSequence(){
        People2Bean p=new People2Bean();
        p.setAge(30);
        p.setName(" ");

        System.out.println("----已婚情况:");
        p.setIsMarried(true);
        Set<ConstraintViolation<People2Bean>> result=validator.validate(p);
        System.out.println("遍历输出错误信息：");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));

        System.out.println("----未婚情况:");
        p.setIsMarried(false);
        result=validator.validate(p);
        System.out.println("遍历输出错误信息：");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));
    }

    @Test
    public void testSelfDef() {
        Person2Bean s=new Person2Bean();
        s.setName("张三");
        //s.setSex("M");
        s.setFriendNames(Stream.of("zhangsan","李四思","张").collect(Collectors.toList()));

        Set<ConstraintViolation<Person2Bean>> result=validator.validate(s);

        System.out.println("遍历输出错误信息:");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));
    }

    @Test
    public void testSelfDef2() {
        OrderBean o=new OrderBean();
        o.setGoodsName("辣条");
        o.setGoodsCount(5);
        o.setGoodsPrice(1.5);
        o.setPrice(20.5);

        Set<ConstraintViolation<OrderBean>> result=validator.validate(o);

        System.out.println("遍历输出错误信息:");
        result.forEach(r-> System.out.println(r.getPropertyPath()+":"+r.getMessage()));
    }
}
