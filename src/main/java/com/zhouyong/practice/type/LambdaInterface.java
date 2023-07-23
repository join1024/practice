package com.zhouyong.practice.type;

import java.io.Serializable;

@FunctionalInterface
public interface LambdaInterface<T,S,R> extends Serializable {

    R apply(T t, S s);

}
