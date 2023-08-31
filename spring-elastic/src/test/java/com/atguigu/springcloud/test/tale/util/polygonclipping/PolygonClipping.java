package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

public final class PolygonClipping {

    private PolygonClipping() {
        throw new AssertionError("No Instances.");
    }

    @SafeVarargs
    public static List<List<Point>> union(List<List<Point>> geom, List<List<Point>>... moreGeoms) {
        if (moreGeoms == null || moreGeoms.length == 0) {
            return geom;
        }
        return null;
    }

    @SafeVarargs
    public static List<List<Point>> intersection(List<List<Point>> geom, List<List<Point>>... moreGeoms) {
        if (moreGeoms == null || moreGeoms.length == 0) {
            return geom;
        }
        return null;
    }

    @SafeVarargs
    public static List<List<Point>> xor(List<List<Point>> geom, List<List<Point>>... moreGeoms) {
        if (moreGeoms == null || moreGeoms.length == 0) {
            return geom;
        }
        return null;
    }

    @SafeVarargs
    public static List<List<Point>> difference(List<List<Point>> subjectGeom, List<List<Point>>... clippingGeoms) {
        if (clippingGeoms == null || clippingGeoms.length == 0) {
            return subjectGeom;
        }
        return null;
    }

}
