package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.shape.Polygon;
import com.atguigu.springcloud.test.tale.util.Units;

import java.util.ArrayList;
import java.util.List;

public final class TaleTransformation {

    private static final int DEFAULT_STEPS = 64;

    private TaleTransformation() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 给定一个中心点和要求的半径，使用默认的64步长绘制一个多边形的圆
     * @param center 中心点位
     * @param radius 半径，单位公里
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius) {
        return circle(center, radius, DEFAULT_STEPS, Units.KILOMETERS);
    }

    /**
     * 给定一个中心点和要求的半径，使用默认的64步长绘制一个多边形的圆
     * @param center 中心点位
     * @param radius 半径
     * @param units  半径单位
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius, Units units) {
        return circle(center, radius, DEFAULT_STEPS, units);
    }

    /**
     * 给定一个中心点和要求的半径，绘制一个多边形的圆
     * @param center 中心点位
     * @param radius 半径
     * @param steps  步长
     * @param units  半径单位
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius, int steps, Units units) {
        List<Point> coordinate = new ArrayList<>(steps + 1);
        for (int i = 0; i < steps; i++) {
            coordinate.add(TaleMeasurement.destination(center, radius, i * 360d / steps, units));
        }

        if (coordinate.size() > 0) {
            coordinate.add(Point.fromLngLat(coordinate.get(0)));
        }
        return Polygon.fromLngLats(coordinate);
    }

    /**
     * 深度克隆一个 geometry 对象
     * @param geometry 图形组件
     * @return 返回克隆后的图形组件
     */
    public static <T extends Geometry> T clone(T geometry) {
        // TODO
        return null;
    }

}
