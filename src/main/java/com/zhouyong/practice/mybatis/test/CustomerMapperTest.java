package com.zhouyong.practice.mybatis.test;

import com.zhouyong.practice.mybatis.base.MybatisBaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author zhouyong
 * @date 2023/7/23 12:32 下午
 */
public class CustomerMapperTest extends MybatisBaseTest {

    /**
     * 支持自动注入Mapper对象
     */
    @Resource
    private CustomerMapper customerMapper;

    @Test
    public void test1(){
        List<CustomerEntity> list = customerMapper.selectAll();
        System.out.println("1 list.size()=="+list.size());

        CustomerEntity entity = new CustomerEntity();
        entity.setName("李四");
        entity.setAge(55);
        entity.setSex("男");

        customerMapper.insertMetrics(entity);

        list = customerMapper.selectAll();
        System.out.println("2 list.size()=="+list.size());
    }

    @Test
    public void test2(){
        List<CustomerEntity> metricsEntities = customerMapper.selectAll();
        System.out.println("3 list.size()=="+metricsEntities.size());

        CustomerEntity entity = new CustomerEntity();
        entity.setName("王五");
        entity.setAge(55);
        entity.setSex("男");

        customerMapper.insertMetrics(entity);

        metricsEntities = customerMapper.selectAll();
        System.out.println("4 list.size()=="+metricsEntities.size());
    }
}
