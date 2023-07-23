package com.zhouyong.practice.date;

import org.junit.Test;

import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.TimeZone;

/**
 * @author join
 * @date 2022/3/6 4:19 下午
 */
public class ZoneTest {

    @Test
    public void test(){
        ZoneId zoneId = ZoneId.of("Europe/Rome");
        System.out.println(zoneId.getRules());

        System.out.println(TimeZone.getDefault().getDisplayName());
    }
}
