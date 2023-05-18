package com.atguigu.springcloud.test.tale.shape;

public interface Geometry<T> {

    T coordinates();

    GeometryType type();

}
