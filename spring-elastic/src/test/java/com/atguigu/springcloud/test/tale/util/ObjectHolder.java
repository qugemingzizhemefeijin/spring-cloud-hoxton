package com.atguigu.springcloud.test.tale.util;

public final class ObjectHolder<T> {

    public T value;

    public ObjectHolder() {

    }

    public ObjectHolder(T initValue) {
        value = initValue;
    }

}
