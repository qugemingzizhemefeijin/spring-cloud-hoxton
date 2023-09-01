package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

public final class Operation {

    // 限制迭代过程以防止无限循环 - 通常由浮点数学舍入误差引起。
    public static final int POLYGON_CLIPPING_MAX_QUEUE_SIZE = 1000000;
    public static final int POLYGON_CLIPPING_MAX_SWEEPLINE_SEGMENTS = 1000000;

    public static List<List<Point>> run(PolygonClippingType type, List<List<Point>> geom, List<List<Point>> moreGeoms) {
        return null;
    }
}
