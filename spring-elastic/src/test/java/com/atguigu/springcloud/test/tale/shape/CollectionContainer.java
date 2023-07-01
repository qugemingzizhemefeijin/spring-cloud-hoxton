package com.atguigu.springcloud.test.tale.shape;

import java.util.List;

public interface CollectionContainer extends Geometry {

    List<Geometry> geometries();

}
