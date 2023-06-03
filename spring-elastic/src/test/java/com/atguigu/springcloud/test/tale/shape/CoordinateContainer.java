package com.atguigu.springcloud.test.tale.shape;

public interface CoordinateContainer<T, V extends Geometry> extends Geometry {

    T coordinates();

    void setCoordinates(T coordinates);

    V deepClone();

}
