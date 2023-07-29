package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.BoundingBox;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public final class TaleHelper {

    private TaleHelper() {
        throw new AssertionError("No Instances.");
    }

    public static final double EARTH_RADIUS = 6371008.8;

    private static final Map<Units, Double> FACTORS = Maps.newHashMap();

    static {
        FACTORS.put(Units.CENTIMETERS, EARTH_RADIUS * 100);
        FACTORS.put(Units.CENTIMETRES, EARTH_RADIUS * 100);
        FACTORS.put(Units.DEGREES, EARTH_RADIUS / 111325);
        FACTORS.put(Units.FEET, EARTH_RADIUS * 3.28084);
        FACTORS.put(Units.INCHES, EARTH_RADIUS * 39.37);
        FACTORS.put(Units.KILOMETERS, EARTH_RADIUS / 1000);
        FACTORS.put(Units.KILOMETRES, EARTH_RADIUS / 1000);
        FACTORS.put(Units.METERS, EARTH_RADIUS);
        FACTORS.put(Units.METRES, EARTH_RADIUS);
        FACTORS.put(Units.MILES, EARTH_RADIUS / 1609.344);
        FACTORS.put(Units.MILLIMETERS, EARTH_RADIUS * 1000);
        FACTORS.put(Units.MILLIMETRES, EARTH_RADIUS * 1000);
        FACTORS.put(Units.NAUTICAL_MILES, EARTH_RADIUS / 1852);
        FACTORS.put(Units.RADIANS, 1D);
        FACTORS.put(Units.YARDS, EARTH_RADIUS * 1.0936);
    }

    /**
     * 将距离测量值（假设地球为球形）从弧度转换为公里单位。
     *
     * @param radians 弧度值
     * @return double
     */
    public static double radiansToLength(double radians) {
        return radiansToLength(radians, Units.KILOMETERS);
    }

    /**
     * 将距离测量值（假设地球为球形）从弧度转换为更友好的单位。
     * 有效单位：MILES、NAUTICAL_MILES、INCHES、YARDS、METERS、METRES、KILOMETERS、CENTIMETERS、FEET
     *
     * @param radians 弧度值
     * @param units   单位
     * @return double
     */
    public static double radiansToLength(double radians, Units units) {
        if (units == null) {
            units = Units.KILOMETERS;
        }

        Double factor = FACTORS.get(units);
        if (factor == null) {
            throw new IllegalArgumentException(units + " units is invalid");
        }

        return radians * factor;
    }

    /**
     * 将以度为单位的角度转换为弧度
     *
     * @param degrees 度数
     * @return double 弧度
     */
    public static double degreesToRadians(double degrees) {
        return (degrees % 360 * Math.PI) / 180;
    }

    /**
     * 角度转换为弧度
     *
     * @param angle 角度
     * @return double 弧度
     */
    public static double angleToRadians(double angle) {
        return angle * Math.PI / 180;
    }

    /**
     * 将以弧度为单位的角度转换为度
     *
     * @param radians 弧度
     * @return double 角度
     */
    public static double radiansToDegrees(double radians) {
        double degrees = radians % (2 * Math.PI);
        return degrees * 180 / Math.PI;
    }

    /**
     * 将距离测量值（假设地球为球形）从实际单位转换为弧度。
     * 有效单位： MILES, NAUTICAL_MILES, INCHES, YARDS, METERS, METRES, KILOMETERS, CENTIMETERS, FEET
     *
     * @param distance 对应单位的距离
     * @param units    距离对应的单位
     * @return double
     */
    public static double lengthToRadians(double distance, Units units) {
        if (units == null) {
            units = Units.KILOMETERS;
        }

        Double factor = FACTORS.get(units);
        if (factor == null) {
            throw new Error(units + " units is invalid");
        }
        return distance / factor;
    }

    /**
     * 将距离测量值（假设地球为球形）从实际单位转换为度。
     * 有效单位： MILES, NAUTICAL_MILES, INCHES, YARDS, METERS, METRES, KILOMETERS, CENTIMETERS, FEET
     *
     * @param distance 对应单位的距离
     * @param units    距离对应的单位
     * @return double 角度
     */
    public static double lengthToDegrees(double distance, Units units) {
        return radiansToDegrees(lengthToRadians(distance, units));
    }

    /**
     * 将轴角度转化为方位角
     *
     * @param bearing 轴角度，介于 -180 和 +180 度之间
     * @return 0 到 360 度之间的角度
     */
    public static double bearingToAzimuth(double bearing) {
        double angle = bearing % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * 四舍五入小数
     *
     * @param number 数值
     * @return 舍入后的数值
     */
    public static double round(double number) {
        return Math.round(number);
    }

    /**
     * 四舍五入并保留指定位数的小数
     *
     * @param number    数值
     * @param precision 保留的小数位
     * @return 舍入后的数值
     */
    public static double round(double number, int precision) {
        if (precision < 0) {
            throw new TaleException("precision must be a positive number");
        } else if (precision == 0) {
            return Math.round(number);
        }

        double multiplier = Math.pow(10, precision);
        return Math.round(number * multiplier) / multiplier;
    }

    /**
     * 验证数组是否是一个BBox数组
     *
     * @param bbox double数组
     */
    public static void validateBBox(double[] bbox) {
        if (bbox.length != 4) {
            throw new TaleException("bbox must be an Array of 4 numbers");
        }
    }

    /**
     * 比较两个坐标点是否一致
     *
     * @param pt1 Point
     * @param pt2 Point
     * @return 一致返回true
     */
    public static boolean equals(Point pt1, Point pt2) {
        return pt1.getLongitude() == pt2.getLongitude() && pt1.getLatitude() == pt2.getLatitude();
    }

    /**
     * 深度比较两个点集合是否一致
     *
     * @param p1 点集合1
     * @param p2 点集合2
     * @return 完全一致则返回true
     */
    public static boolean deepEquals(List<Point> p1, List<Point> p2) {
        if (p1 == null && p2 == null) {
            return true;
        } else if (p1 != null && p2 == null) {
            return false;
        } else if (p1 == null) {
            return false;
        }

        // 两个均不为空
        if (p1.size() != p2.size()) {
            return false;
        }

        for (int i = 0, size = p1.size(); i < size; i++) {
            if (!equals(p1.get(i), p2.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 验证多边形是否是首尾相连
     *
     * @param points 多边形点集合
     * @return 如果收尾相连则返回true
     */
    public static boolean validatePolygonEndToEnd(List<Point> points) {
        if (points == null || points.size() < 2) {
            throw new TaleException("points must be an Array of 2 numbers");
        }

        Point first = points.get(0), end = points.get(points.size() - 1);

        return first.getLongitude() == end.getLongitude() && first.getLatitude() == end.getLatitude();
    }

    /**
     * 判断一个点是否在BBox矩形中
     *
     * @param point 要验证的点
     * @param bbox  矩形
     * @return true则在矩形中，否则为false
     */
    public static boolean inBBox(Point point, BoundingBox bbox) {
        double[] b = bbox.bbox();
        double longitude = point.getLongitude(), latitude = point.getLatitude();

        return b[0] <= longitude && b[1] <= latitude && b[2] >= longitude && b[3] >= latitude;
    }

}
