package com.zhouyong.practice.mybatis.test;

import java.util.List;

/**
 * @author zhouyong
 * @date 2023/7/23 12:24 下午
 */
public interface CustomerMapper {

    List<CustomerEntity> selectAll();

    int insertMetrics(CustomerEntity entity);
}
