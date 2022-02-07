package com.atguigu.springcloud.poi;

@FunctionalInterface
public interface DataAware<T , K> {

    K parse(T t);

}
