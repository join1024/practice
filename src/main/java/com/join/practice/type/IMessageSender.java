package com.join.practice.type;

/**
 * @author join
 * @date 2023/7/9 9:13 下午
 */
public interface IMessageSender<T,V> {
    V sendMsg(T msg);
}
