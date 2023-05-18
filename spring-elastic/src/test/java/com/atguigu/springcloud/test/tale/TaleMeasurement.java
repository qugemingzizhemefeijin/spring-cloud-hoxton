package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.MultiPolygon;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.shape.Polygon;
import com.atguigu.springcloud.test.tale.util.TaleHelper;
import com.atguigu.springcloud.test.tale.util.Units;

import java.util.List;

public final class TaleMeasurement {

    /**
     * 地球半径（以米为单位）
     */
    public static double EARTH_RADIUS = 6378137;

    private TaleMeasurement() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 计算两点之间的距离，默认单位公里
     *
     * @param p1 点1
     * @param p2 点2
     * @return double 默认单位公里
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1, p2, Units.KILOMETERS);
    }

    /**
     * 计算两点之间的距离
     *
     * @param p1    点1
     * @param p2    点2
     * @param units 距离单位
     * @return double
     */
    public static double distance(Point p1, Point p2, Units units) {
        double dLat = TaleHelper.degreesToRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = TaleHelper.degreesToRadians(p2.getLongitude() - p1.getLongitude());
        double lat1 = TaleHelper.degreesToRadians(p1.getLatitude());
        double lat2 = TaleHelper.degreesToRadians(p2.getLatitude());

        double value = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2)
                * Math.cos(lat1)
                * Math.cos(lat2);

        return TaleHelper.radiansToLength(2 * Math.atan2(Math.sqrt(value), Math.sqrt(1 - value)), units);
    }

    /**
     * 计算面积
     * @param geometry 支持POLYGON、MULTI_POLYGON
     * @param units    支持公里、英里、米
     * @return 面积（指定单位）
     */
    public static double area(Geometry<?> geometry, Units units) {
        double area = area(geometry);
        if (units == null) {
            return area;
        }

        switch (units) {
            case KILOMETERS:
            case KILOMETRES:
                return area / 1000000;
            case MILES:
                return area / 2589988.110336D;
            default:
                return area;
        }
    }

    /**
     * 计算面积（以平方米为单位）。
     *
     * @param geometry 支持 POLYGON、MULTI_POLYGON
     * @return 面积（平方米）
     */
    public static double area(Geometry<?> geometry) {
        switch (geometry.type()) {
            case POLYGON:
                return Math.abs(ringArea(((Polygon) geometry).coordinates()));
            case MULTI_POLYGON:
                double total = 0.0D;
                List<List<Point>> coordinates = ((MultiPolygon) geometry).coordinates();
                for (List<Point> coordinate : coordinates) {
                    total += ringArea(coordinate);
                }
                return Math.abs(total);
            default:
                return 0;
        }
    }

    /**
     * 计算投影到地球上的多边形的大致面积。请注意，如果环方向为顺时针方向，则此区域将为正，否则它将是负面的。
     *
     * @param coordinates 坐标点集合
     * @return 面的近似符号测地线面积（以平方米为单位）。
     */
    private static double ringArea(List<Point> coordinates) {
        Point p1, p2, p3;
        int lowerIndex, middleIndex, upperIndex;
        double total = 0.0f;
        final int coordsLength = coordinates.size();

        if (coordsLength > 2) {
            for (int i = 0; i < coordsLength; i++) {
                if (i == coordsLength - 2) { // i = N-2
                    lowerIndex = coordsLength - 2;
                    middleIndex = coordsLength - 1;
                    upperIndex = 0;
                } else if (i == coordsLength - 1) { // i = N-1
                    lowerIndex = coordsLength - 1;
                    middleIndex = 0;
                    upperIndex = 1;
                } else { // i = 0 to N-3
                    lowerIndex = i;
                    middleIndex = i + 1;
                    upperIndex = i + 2;
                }
                p1 = coordinates.get(lowerIndex);
                p2 = coordinates.get(middleIndex);
                p3 = coordinates.get(upperIndex);
                total += (TaleHelper.angleToRadians(p3.getLongitude()) - TaleHelper.angleToRadians(p1.getLongitude())) * Math.sin(TaleHelper.angleToRadians(p2.getLatitude()));
            }
            total = total * EARTH_RADIUS * EARTH_RADIUS / 2;
        }
        return total;
    }

    /**
     * 获取两个点，找出它们之间的地理方位，即从正北算起的角度(0度)
     * @param p1           点1
     * @param p2           点1
     * @return 介于 -180 和 180 度之间（顺时针正方向）
     */
    public static double bearing(Point p1, Point p2) {
        return bearing(p1, p2, false);
    }

    /**
     * 获取两个点，找出它们之间的地理方位，即从正北算起的角度(0度)
     * @param p1           点1
     * @param p2           点1
     * @param finalBearing 计算最终方位角（如果为真）
     * @return 介于 -180 和 180 度之间（顺时针正方向）
     */
    public static double bearing(Point p1, Point p2, boolean finalBearing) {
        if (finalBearing) {
            return calculateFinalBearing(p1, p2);
        }

        double lon1 = TaleHelper.degreesToRadians(p1.getLongitude());
        double lon2 = TaleHelper.degreesToRadians(p2.getLongitude());
        double lat1 = TaleHelper.degreesToRadians(p1.getLatitude());
        double lat2 = TaleHelper.degreesToRadians(p2.getLatitude());
        double value1 = Math.sin(lon2 - lon1) * Math.cos(lat2);
        double value2 = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(lon2 - lon1);

        return TaleHelper.radiansToDegrees(Math.atan2(value1, value2));
    }

    /**
     * 计算最终方位角
     * @param p1 点1
     * @param p2 点2
     * @return 介于 -180 和 180 度之间（顺时针正方向）
     */
    private static double calculateFinalBearing(Point p1, Point p2) {
        // Swap start & end
        double bear = bearing(p2, p1, false);
        bear = (bear + 180) % 360;
        return bear;
    }

}
