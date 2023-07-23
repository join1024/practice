package com.zhouyong.practice.date;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

/**
 * @author join
 * @date 2022/3/6 4:19 下午
 */
public class DateTest {

    @Test
    public void test(){
        Instant instant = new Date().toInstant();
        System.out.println(instant);

    }
}
