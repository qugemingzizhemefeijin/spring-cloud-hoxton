package com.atguigu.springcloud.test.tale.util;

import com.google.common.collect.Maps;

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
     * @param radians 弧度值
     * @return double
     */
    public static double radiansToLength(double radians) {
        return radiansToLength(radians, Units.KILOMETERS);
    }

    /**
     * 将距离测量值（假设地球为球形）从弧度转换为更友好的单位。
     * 有效单位：MILES、NAUTICAL_MILES、INCHES、YARDS、METERS、METRES、KILOMETERS、CENTIMETERS、FEET
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
     * @param degrees 度数
     * @return double 弧度
     */
    public static double degreesToRadians(double degrees) {
        return (degrees % 360 * Math.PI) / 180;
    }

    /**
     * 角度转换为弧度
     * @param angle 角度
     * @return double 弧度
     */
    public static double angleToRadians(double angle) {
        return angle * Math.PI / 180;
    }

    /**
     * 将以弧度为单位的角度转换为度
     * @param radians 弧度
     * @return double 角度
     */
    public static double radiansToDegrees(double radians) {
        double degrees = radians % (2 * Math.PI);
        return degrees * 180 / Math.PI;
    }

}
