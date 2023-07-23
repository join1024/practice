package com.zhouyong.practice.mybatis.test;

import com.zhouyong.practice.mybatis.base.MybatisBaseTest;
import com.zhouyong.practice.mybatis.test.CustomerEntity;
import com.zhouyong.practice.mybatis.test.CustomerMapper;
import org.junit.Test;

import java.util.List;

/**
 *
 * @author zhouyong
 * @date 2023/7/23 12:32 下午
 */
public class CustomerMapperTest extends MybatisBaseTest {

    @Test
    public void test1(){
        CustomerMapper mapper = getMapper(CustomerMapper.class);
        List<CustomerEntity> list = mapper.selectAll();
        System.out.println("1 list.size()=="+list.size());

        CustomerEntity entity = new CustomerEntity();
        entity.setName("李四");
        entity.setAge(55);
        entity.setSex("男");

        mapper.insertMetrics(entity);

        list = mapper.selectAll();
        System.out.println("2 list.size()=="+list.size());
    }

    @Test
    public void test2(){
        CustomerMapper mapper = getMapper(CustomerMapper.class);
        List<CustomerEntity> metricsEntities = mapper.selectAll();
        System.out.println("3 list.size()=="+metricsEntities.size());

        CustomerEntity entity = new CustomerEntity();
        entity.setName("王五");
        entity.setAge(55);
        entity.setSex("男");

        mapper.insertMetrics(entity);

        metricsEntities = mapper.selectAll();
        System.out.println("4 list.size()=="+metricsEntities.size());
    }
}
