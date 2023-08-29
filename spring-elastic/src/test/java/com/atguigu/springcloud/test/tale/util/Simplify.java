package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

/*
 (c) 2013, Vladimir Agafonkin
 Simplify.js, a high-performance JS polyline simplification library
 mourner.github.io/simplify-js
*/

// to suit your point format, run search/replace for '.x' and '.y';
// for 3D version, see 3d branch (configurability would draw significant performance overhead)
// https://github.com/Turfjs/turf/blob/v6.5.0/packages/turf-simplify/lib/simplify.js
public final class Simplify {

    private Simplify() {
        throw new AssertionError("No Instances.");
    }

    public static List<Point> simplify(List<Point> points, int tolerance, boolean highestQuality) {
        return null;
    }

}
