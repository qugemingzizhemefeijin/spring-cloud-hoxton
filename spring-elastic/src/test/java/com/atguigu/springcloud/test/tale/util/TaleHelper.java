package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleBooleans;
import com.atguigu.springcloud.test.tale.TaleMeasurement;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;
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

    /**
     * 判断点是否在线中
     *
     * @param pt   要判断的点
     * @param line 线段
     * @return 如果pt在line中，则返回true
     */
    public static boolean isPointOnLine(Point pt, Line line) {
        List<Point> points = line.coordinates();
        for (int i = 0, size = points.size(); i < size - 1; i++) {
            if (isPointOnLineSegment(points.get(i), points.get(i + 1), pt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断点是否在点组合中
     *
     * @param pt 要判断的点
     * @param mp 点组合
     * @return 如果pt在mp中，则返回true
     */
    public static boolean isPointInMultiPoint(Point pt, MultiPoint mp) {
        for (Point p : mp.coordinates()) {
            if (equals(pt, p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断mp1中的点是否均在mp2中
     *
     * @param mp1 要判断的点组合
     * @param mp2 点组合
     * @return 如果mp1中的所有点均在mp2中，则返回true
     */
    public static boolean isMultiPointInMultiPoint(MultiPoint mp1, MultiPoint mp2) {
        for (Point p1 : mp1.coordinates()) {
            boolean anyMatch = false;
            for (Point p2 : mp2.coordinates()) {
                if (equals(p1, p2)) {
                    anyMatch = true;
                }
            }

            if (!anyMatch) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断点组合中的点是否均在线上（至少有一个点不在开始和结束顶点上）
     *
     * @param mp   要判断的点组合
     * @param line 线段
     * @return 如果mp中所有点均在Line上，则返回true
     */
    public static boolean isMultiPointOnLine(MultiPoint mp, Line line) {
        boolean foundInsidePoint = false;

        for (Point p : mp.coordinates()) {
            if (!TaleBooleans.booleanPointOnLine(p, line)) {
                return false;
            }
            if (!foundInsidePoint) {
                foundInsidePoint = TaleBooleans.booleanPointOnLine(p, line, true);
            }
        }

        return foundInsidePoint;
    }

    /**
     * 判断点组合中的点是否均在多边形中(必须至少一个点在多边形内)
     *
     * @param mp      要判断的点组合
     * @param polygon 多边形
     * @return 如果mp中所有点均在polygon中，则返回true
     */
    public static boolean isMultiPointInPolygon(MultiPoint mp, Polygon polygon) {
        boolean oneInside = false;
        for (Point p : mp.coordinates()) {
            if (!TaleBooleans.booleanPointInPolygon(p, polygon)) {
                return false;
            }
            if (!oneInside) {
                oneInside = TaleBooleans.booleanPointInPolygon(p, polygon, true);
            }
        }
        return oneInside;
    }

    /**
     * 判断点组合中的点是否均在多边形中(必须至少一个点在多边形内)
     *
     * @param mp           要判断的点组合
     * @param multiPolygon 组合多边形
     * @return 如果mp中所有点均在polygon中，则返回true
     */
    public static boolean isMultiPointInPolygon(MultiPoint mp, MultiPolygon multiPolygon) {
        boolean oneInside = false;
        for (Point p : mp.coordinates()) {
            if (!TaleBooleans.booleanPointInPolygon(p, multiPolygon)) {
                return false;
            }
            if (!oneInside) {
                oneInside = TaleBooleans.booleanPointInPolygon(p, multiPolygon, true);
            }
        }
        return oneInside;
    }

    /**
     * 判断线段1是否在线段2上
     *
     * @param line1 要判断的线段
     * @param line2 线段
     * @return 如果line1完全在line2中，则返回true
     */
    public static boolean isLineOnLine(Line line1, Line line2) {
        for (Point p : line1.coordinates()) {
            if (!TaleBooleans.booleanPointOnLine(p, line2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断线段是否在多边形中
     *
     * @param line    要判断的线段
     * @param polygon 多边形
     * @return 如果line在多边形内，则返回true
     */
    public static boolean isLineInPolygon(Line line, Polygon polygon) {
        return isLineInPoly(line, polygon);
    }

    /**
     * 判断线段是否在多边形中
     *
     * @param line         要判断的线段
     * @param multiPolygon 组合多边形
     * @return 如果line在多边形内，则返回true
     */
    public static boolean isLineInPolygon(Line line, MultiPolygon multiPolygon) {
        return isLineInPoly(line, multiPolygon);
    }

    /**
     * 判断线段是否在多边形中
     *
     * @param line     要判断的线段
     * @param geometry 多边形，只支持Polygon、MultiPolygon
     * @return 如果line在多边形内，则返回true
     */
    private static boolean isLineInPoly(Line line, Geometry geometry) {
        BoundingBox polyBbox = TaleMeasurement.bbox(geometry);
        BoundingBox lineBbox = TaleMeasurement.bbox(line);
        if (!isBBoxOverlap(polyBbox, lineBbox)) {
            return false;
        }

        boolean foundInsidePoint = false;
        List<Point> coordinates = line.coordinates();
        for (int i = 0, size = coordinates.size() - 1; i < size; i++) {
            Point p = coordinates.get(i);
            // 如果点不在多边形中，直接返回
            if (!TaleBooleans.booleanPointInPolygon(p, geometry, false)) {
                return false;
            }
            // 下面军事判断点是否在多边形内（不能在边界上）
            if (!foundInsidePoint) {
                foundInsidePoint = TaleBooleans.booleanPointInPolygon(p, geometry, true);
            }
            // 跑到这里，证明点在多边形的边界上，则计算当前点与下一个点的中间点是否在多边形上，如果在的话，则证明先在多边形内。
            if (!foundInsidePoint) {
                Point midPoint = getMidPoint(p, coordinates.get(i + 1));
                foundInsidePoint = TaleBooleans.booleanPointInPolygon(midPoint, geometry, true);
            }
        }

        return foundInsidePoint;
    }

    /**
     * 判断多边形1是否在多边形2中
     *
     * @param polygon1 要判断的多边形
     * @param polygon2 多边形，支持Polygon、MultiPolygon
     * @return 如果多边形1在多边形2中，则返回true
     */
    public static boolean isPolygonInPolygon(Polygon polygon1, Geometry polygon2) {
        BoundingBox poly1Bbox = TaleMeasurement.bbox(polygon1);
        BoundingBox poly2Bbox = TaleMeasurement.bbox(polygon2);
        if (!isBBoxOverlap(poly2Bbox, poly1Bbox)) {
            return false;
        }

        for (Point p : polygon1.coordinates()) {
            if (!TaleBooleans.booleanPointInPolygon(p, polygon2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断bbox1是否覆盖了bbox2
     *
     * @param bbox1 矩形Box1
     * @param bbox2 矩形Box2
     * @return bbox1覆盖了bbox2则返回true
     */
    public static boolean isBBoxOverlap(BoundingBox bbox1, BoundingBox bbox2) {
        if (bbox1.get(0) > bbox2.get(0)) {
            return false;
        }
        if (bbox1.get(2) < bbox2.get(2)) {
            return false;
        }
        if (bbox1.get(1) > bbox2.get(1)) {
            return false;
        }
        if (bbox1.get(3) < bbox2.get(3)) {
            return false;
        }
        return true;
    }

    /**
     * 计算两个点之间的中间点，仅仅是两个点的位置/2。与 #{@link TaleMeasurement#midpoint } 是有区别的
     *
     * @param p1 点1
     * @param p2 点2
     * @return 返回两点之间的中点
     */
    public static Point getMidPoint(Point p1, Point p2) {
        return Point.fromLngLat((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
    }

    /**
     * 判断point是否位于start和end之间的线段上（不忽略线的两段）
     *
     * @param start 开始的点
     * @param end   结束的点
     * @param point 需要判断的点
     * @return 在线段上则返回true
     */
    public static boolean isPointOnLineSegment(Point start, Point end, Point point) {
        double x = point.getLongitude(), y = point.getLatitude();
        double startX = start.getLongitude(), startY = start.getLatitude();
        double endX = end.getLongitude(), endY = end.getLatitude();

        double dxc = x - startX;
        double dyc = y - startY;
        double dxl = endX - startX;
        double dyl = endY - startY;
        double cross = dxc * dyl - dyc * dxl;

        if (cross != 0) {
            return false;
        } else if (Math.abs(dxl) >= Math.abs(dyl)) {
            return dxl > 0 ? startX <= x && x <= endX : endX <= x && x <= startX;
        } else {
            return dyl > 0 ? startY <= y && y <= endY : endY <= y && y <= startY;
        }
    }

    /**
     * 判断点是否在线的两端之间
     *
     * @param lineSegmentStart 线段开始坐标对的行首
     * @param lineSegmentEnd   线段结束坐标对的行尾
     * @param point            判断的点
     * @param incEndVertices   是否允许点落在线两端
     * @return boolean
     */
    public static boolean isPointOnLineSegment(Point lineSegmentStart, Point lineSegmentEnd, Point point, boolean incEndVertices) {
        double x = point.getX(), y = point.getY();
        double x1 = lineSegmentStart.getX(), y1 = lineSegmentStart.getY();
        double x2 = lineSegmentEnd.getX(), y2 = lineSegmentEnd.getY();
        double dxc = x - x1, dyc = y - y1;
        double dxl = x2 - x1, dyl = y2 - y1;
        double cross = dxc * dyl - dyc * dxl;

        if (cross != 0) {
            return false;
        }

        boolean dxlGtdyl = Math.abs(dxl) >= Math.abs(dyl);
        // 是否允许判断两端
        if (incEndVertices) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 <= x && x <= x2 : x2 <= x && x <= x1;
            }
            return dyl > 0 ? y1 <= y && y <= y2 : y2 <= y && y <= y1;
        } else {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 < x && x < x2 : x2 < x && x < x1;
            }
            return dyl > 0 ? y1 < y && y < y2 : y2 < y && y < y1;
        }
    }

    /**
     * 判断点是否在线的两端之间
     *
     * @param lineSegmentStart 线段开始坐标对的行首
     * @param lineSegmentEnd   线段结束坐标对的行尾
     * @param point            判断的点
     * @param excludeBoundary  排除边界是否允许点落在线端，0不忽略，1开头，2结尾，3中间
     * @param epsilon          要与交叉乘积结果进行比较的小数。用于处理浮点，例如经纬度点
     * @return boolean
     */
    public static boolean isPointOnLineSegment(Point lineSegmentStart, Point lineSegmentEnd, Point point, int excludeBoundary, Number epsilon) {
        double x = point.getX(), y = point.getY();
        double x1 = lineSegmentStart.getX(), y1 = lineSegmentStart.getY();
        double x2 = lineSegmentEnd.getX(), y2 = lineSegmentEnd.getY();
        double dxc = x - x1, dyc = y - y1;
        double dxl = x2 - x1, dyl = y2 - y1;
        double cross = dxc * dyl - dyc * dxl;

        if (epsilon != null) {
            if (Math.abs(cross) > epsilon.doubleValue()) {
                return false;
            }
        } else if (cross != 0) {
            return false;
        }

        boolean dxlGtdyl = Math.abs(dxl) >= Math.abs(dyl);
        if (excludeBoundary == 0) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 <= x && x <= x2 : x2 <= x && x <= x1;
            }
            return dyl > 0 ? y1 <= y && y <= y2 : y2 <= y && y <= y1;
        } else if (excludeBoundary == 1) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 < x && x <= x2 : x2 <= x && x < x1;
            }
            return dyl > 0 ? y1 < y && y <= y2 : y2 <= y && y < y1;
        } else if (excludeBoundary == 2) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 <= x && x < x2 : x2 < x && x <= x1;
            }
            return dyl > 0 ? y1 <= y && y < y2 : y2 < y && y <= y1;
        } else if (excludeBoundary == 3) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 < x && x < x2 : x2 < x && x < x1;
            }
            return dyl > 0 ? y1 < y && y < y2 : y2 < y && y < y1;
        }

        return false;
    }

}
