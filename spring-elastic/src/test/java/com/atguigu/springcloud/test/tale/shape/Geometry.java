package com.atguigu.springcloud.test.tale.shape;

import com.atguigu.springcloud.test.tale.exception.TaleException;

public interface Geometry {

    GeometryType type();

    /**
     * 坐标点的数量，仅适合Point、Line、Polygon、MultiPoint
     *
     * @return int
     */
    default int coordsSize() {
        throw new TaleException("geometry not support coordsSize");
    }

}
