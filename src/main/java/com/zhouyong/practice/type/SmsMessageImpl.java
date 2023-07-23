package com.zhouyong.practice.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author join
 * @date 2023/7/9 9:15 下午
 */
public class SmsMessageImpl implements IMessageSender<String, List<Integer>>, Serializable {
    @Override
    public List<Integer> sendMsg(String msg) {
        return new ArrayList<>();
    }
}
