package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.*;
import com.atguigu.springcloud.test.tale.util.GreatCircle;
import com.atguigu.springcloud.test.tale.util.TaleHelper;
import com.atguigu.springcloud.test.tale.util.Units;

import java.util.Arrays;
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
     * 计算两点之间的距离，默认单位公里。该方法使用哈弗赛因公式来考虑全球曲率。
     *
     * @param p1 点1
     * @param p2 点2
     * @return double 默认单位公里
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1, p2, Units.KILOMETERS);
    }

    /**
     * 计算两点之间的距离。该方法使用哈弗赛因公式来考虑全球曲率。
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
     * 计算两个点之间的大圆距离，单位公里。
     *
     * @param from  起始点
     * @param to    目标点
     * @return 返回两点之间的距离（公里）
     */
    public static double rhumbDistance(Point from, Point to) {
        return rhumbDistance(from, to, Units.KILOMETERS);
    }

    /**
     * 计算两个点之间的大圆距离(以度、弧度、英里或公里为单位)。
     *
     * @param from  起始点
     * @param to    目标点
     * @param units 距离单位，支持 DEGREES、RADIANS、MILES、KILOMETERS
     * @return 返回两点之间的距离
     */
    public static double rhumbDistance(Point from, Point to, Units units) {
        // compensate the crossing of the 180th meridian (https://macwright.org/2016/09/26/the-180th-meridian.html)
        // solution from https://github.com/mapbox/mapbox-gl-js/issues/3250#issuecomment-294887678
        double destX = to.getX() + (to.getX() - from.getX() > 180 ? -360 : from.getX() - to.getX() > 180 ? 360 : 0);
        double destY = to.getY();

        double distanceInMeters = calculateRhumbDistance(from, Point.fromLngLat(destX, destY), null);

        return TaleUnitConversion.convertLength(distanceInMeters, Units.METERS, units);
    }

    /**
     * 返回沿恒向线从起始点点到目标点的距离。<br>
     * Adapted from Geodesy: https://github.com/chrisveness/geodesy/blob/master/latlon-spherical.js
     *
     * @param origin      起始点
     * @param destination 目标点
     * @param radius      地球半径（默认为以米为单位的半径）。
     * @return 返回两点之间的距离，单位为米
     */
    private static double calculateRhumbDistance(Point origin, Point destination, Double radius) {
        // φ => phi
        // λ => lambda
        // ψ => psi
        // Δ => Delta
        // δ => delta
        // θ => theta
        double R = radius == null ? TaleHelper.EARTH_RADIUS : radius;
        double phi1 = (origin.getY() * Math.PI) / 180;
        double phi2 = (destination.getY() * Math.PI) / 180;
        double DeltaPhi = phi2 - phi1;
        double DeltaLambda = (Math.abs(destination.getX() - origin.getX()) * Math.PI) / 180;
        // if dLon over 180° take shorter rhumb line across the anti-meridian:
        if (DeltaLambda > Math.PI) {
            DeltaLambda -= 2 * Math.PI;
        }

        // on Mercator projection, longitude distances shrink by latitude; q is the 'stretch factor'
        // q becomes ill-conditioned along E-W line (0/0); use empirical tolerance to avoid it
        double DeltaPsi = Math.log(Math.tan(phi2 / 2 + Math.PI / 4) / Math.tan(phi1 / 2 + Math.PI / 4));
        double q = Math.abs(DeltaPsi) > 10e-12 ? DeltaPhi / DeltaPsi : Math.cos(phi1);

        // distance is pythagoras on 'stretched' Mercator projection
        double delta = Math.sqrt(DeltaPhi * DeltaPhi + q * q * DeltaLambda * DeltaLambda); // angular distance in radians

        return delta * R;
    }

    /**
     * 计算面积
     *
     * @param geometry 支持POLYGON、MULTI_POLYGON
     * @param units    支持公里、英里、米
     * @return 面积（指定单位）
     */
    public static double area(Geometry geometry, Units units) {
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
    public static double area(Geometry geometry) {
        double total = 0.0D;

        switch (geometry.type()) {
            case POLYGON:
                total = Math.abs(ringArea(((Polygon) geometry).coordinates()));
                break;
            case MULTI_POLYGON:
                List<List<Point>> coordinates = ((MultiPolygon) geometry).coordinates();
                for (List<Point> coordinate : coordinates) {
                    total += ringArea(coordinate);
                }
                total = Math.abs(total);
                break;
            case GEOMETRY_COLLECTION:
                for (Geometry singleGeometry : ((GeometryCollection) geometry).geometries()) {
                    total += area(singleGeometry);
                }
                total = Math.abs(total);
                break;
        }

        return total;
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
     *
     * @param p1 点1
     * @param p2 点1
     * @return 介于 -180 和 180 度之间（顺时针正方向）
     */
    public static double bearing(Point p1, Point p2) {
        return bearing(p1, p2, false);
    }

    /**
     * 获取两个点，找出它们之间的地理方位，即从正北算起的角度(0度)
     *
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
     *
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

    /**
     * 获取一个点并计算给定距离(以度、弧度、英里或公里为单位)的目标点的位置。
     *
     * @param p        开始点
     * @param distance 与开始点的相距公里数
     * @param bearing  角度，范围从 -180 到 180
     * @return Point
     */
    public static Point destination(Point p, double distance, double bearing) {
        return destination(p, distance, bearing, null);
    }

    /**
     * 获取一个点并计算给定距离(以度、弧度、英里或公里为单位)的目标点的位置。
     *
     * @param p        开始点
     * @param distance 与开始点的距离
     * @param bearing  角度，范围从 -180 到 180
     * @param units    距离单位
     * @return Point
     */
    public static Point destination(Point p, double distance, double bearing, Units units) {
        double longitude1 = TaleHelper.degreesToRadians(p.getLongitude());
        double latitude1 = TaleHelper.degreesToRadians(p.getLatitude());
        double bearingRad = TaleHelper.degreesToRadians(bearing);
        double radians = TaleHelper.lengthToRadians(distance, units);

        // Main
        double latitude2 = Math.asin(Math.sin(latitude1)
                * Math.cos(radians)
                + Math.cos(latitude1)
                * Math.sin(radians)
                * Math.cos(bearingRad));

        double longitude2 = longitude1 + Math.atan2(
                Math.sin(bearingRad) * Math.sin(radians) * Math.cos(latitude1),
                Math.cos(radians) - Math.sin(latitude1) * Math.sin(latitude2));

        double lng = TaleHelper.radiansToDegrees(longitude2);
        double lat = TaleHelper.radiansToDegrees(latitude2);

        return new Point(lng, lat);
    }

    /**
     * 获取两个点并返回中间的一个点。中点是测地线计算的，这意味着地球的曲率也被考虑在内
     *
     * @param p1 点1
     * @param p2 点2
     * @return 两点的中间点
     */
    public static Point midpoint(Point p1, Point p2) {
        double dist = distance(p1, p2);
        double heading = bearing(p1, p2);

        return destination(p1, dist / 2, heading, null);
    }

    /**
     * 两个点，找出它们之间沿恒向线的夹角，即从北线开始(0度)测量的角度。默认情况不计算最终方位角
     *
     * @param start 开始点
     * @param end   目标点
     * @return 从北线计算的方位，介于 -180 和 180 度之间（顺时针正方向）
     */
    public static double rhumbBearing(Point start, Point end) {
        return rhumbBearing(start, end, false);
    }

    /**
     * 两个点，找出它们之间沿恒向线的夹角，即从北线开始(0度)测量的角度。
     *
     * @param start        开始点
     * @param end          目标点
     * @param finalBearing 是否计算最终方位角
     * @return 从北线计算的方位，介于 -180 和 180 度之间（顺时针正方向）
     */
    public static double rhumbBearing(Point start, Point end, boolean finalBearing) {
        double bear360;
        if (finalBearing) {
            bear360 = calculateRhumbBearing(end, start);
        } else {
            bear360 = calculateRhumbBearing(start, end);
        }

        return bear360 > 180 ? -(360 - bear360) : bear360;
    }

    /**
     * 沿恒向线从开始点返回到目标点的方位角。
     *
     * @param start 开始点
     * @param end   目标点
     * @return 返回从北开始的度
     */
    private static double calculateRhumbBearing(Point start, Point end) {
        double phi1 = TaleHelper.degreesToRadians(start.getLatitude());
        double phi2 = TaleHelper.degreesToRadians(end.getLatitude());
        double deltaLambda = TaleHelper.degreesToRadians(end.getLongitude() - start.getLongitude());
        // if deltaLambdaon over 180° take shorter rhumb line across the anti-meridian:
        if (deltaLambda > Math.PI) {
            deltaLambda -= 2 * Math.PI;
        }
        if (deltaLambda < -Math.PI) {
            deltaLambda += 2 * Math.PI;
        }

        double deltaPsi = Math.log(Math.tan(phi2 / 2 + Math.PI / 4) / Math.tan(phi1 / 2 + Math.PI / 4));
        double theta = Math.atan2(deltaLambda, deltaPsi);

        return (TaleHelper.radiansToDegrees(theta) + 360) % 360;
    }

    /**
     * 获取多边形并以公里单位测量其周长
     *
     * @param geometry 支持 LINE、POLYGON、MULTI_LINE、MULTI_POLYGON
     * @return 以公里表示的输入面的总周长
     */
    public static double length(Geometry geometry) {
        return length(geometry, Units.KILOMETERS);
    }

    /**
     * 获取多边形并以指定单位测量其周长。
     *
     * @param geometry 支持 LINE、POLYGON、MULTI_LINE、MULTI_POLYGON
     * @param units    距离单位
     * @return 以指定单位表示的输入面的总周长
     */
    public static double length(Geometry geometry, Units units) {
        switch (geometry.type()) {
            case LINE:
                return length(((Line) geometry).coordinates(), units);
            case POLYGON:
                return length(((Polygon) geometry).coordinates(), units);
            case MULTI_POINT:
                return length(((MultiPoint) geometry).coordinates(), units);
            case MULTI_LINE:
                return lengthList(((MultiLine) geometry).coordinates(), units);
            case MULTI_POLYGON:
                return lengthList(((MultiPolygon) geometry).coordinates(), units);
            case GEOMETRY_COLLECTION:
                double total = 0;
                for (Geometry singleGeometry : ((GeometryCollection) geometry).geometries()) {
                    total += length(singleGeometry, units);
                }
                return total;
            default:
                return 0;
        }
    }

    /**
     * 获取多点集合并以指定单位测量其周长
     *
     * @param coordinates 多点集合
     * @param units       距离单位
     * @return 以指定单位表示的输入面的总周长
     */
    public static double lengthList(List<List<Point>> coordinates, Units units) {
        double total = 0;
        for (List<Point> coords : coordinates) {
            total += length(coords, units);
        }
        return total;
    }

    /**
     * 获取点集合并以指定的单位测量其长度。
     *
     * @param coords 点集合
     * @param units  距离单位
     * @return 以指定单位表示的长度
     */
    public static double length(List<Point> coords, Units units) {
        double travelled = 0;
        Point prevCoords = coords.get(0);
        Point curCoords;
        for (int i = 1; i < coords.size(); i++) {
            curCoords = coords.get(i);
            travelled += distance(prevCoords, curCoords, units);
            prevCoords = curCoords;
        }
        return travelled;
    }

    /**
     * 使用多边形并计算绝对中心点
     *
     * @param polygon 多边形
     * @return 位于所有输入要素绝对中心点
     */
    public static Point center(Polygon polygon) {
        BoundingBox bbox = bbox(polygon);
        double x = (bbox.west() + bbox.east()) / 2;
        double y = (bbox.south() + bbox.north()) / 2;
        return Point.fromLngLat(x, y);
    }

    /**
     * 获取一个图形计算并返回一个边界框(西南东北)
     *
     * @param geometry 支持Line、MultiLine、Polygon、MultiPolygon
     * @return minX, minY, maxX, maxY
     */
    public static BoundingBox bbox(Geometry geometry) {
        switch (geometry.type()) {
            case LINE:
                return bboxCalculator(TaleMeta.coordAll((Line) geometry));
            case POLYGON:
                return bboxCalculator(TaleMeta.coordAll((Polygon) geometry, false));
            case MULTI_LINE:
                return bboxCalculator(TaleMeta.coordAll((MultiLine) geometry));
            case MULTI_POLYGON:
                return bboxCalculator(TaleMeta.coordAll((MultiPolygon) geometry, false));
            case MULTI_POINT:
                return bboxCalculator(TaleMeta.coordAll((MultiPoint) geometry));
            case GEOMETRY_COLLECTION:
                return bboxCalculator(TaleMeta.coordAll((GeometryCollection) geometry, false));
            default:
                throw new RuntimeException(("Unknown geometry class: " + geometry.getClass()));
        }
    }

    /**
     * 根据bbox并返回一个等价的多边形
     *
     * @param bbox 左下右上经纬度(西南，东北)
     * @return 边界框的多边形
     */
    public static Polygon bboxPolygon(BoundingBox bbox) {
        return Polygon.fromLngLats(Arrays.asList(
                bbox.getSouthwest(), // 西南点
                bbox.getSoutheast(), // 东南点
                bbox.getNortheast(), // 东北点
                bbox.getNorthwest(), // 西北点
                bbox.getSouthwest())); // 需要闭合
    }

    /**
     * 根据传入的点集合计算组合成最大坐标点和最小坐标点
     *
     * @param resultCoords 点集合
     * @return BoundingBox
     */
    private static BoundingBox bboxCalculator(List<Point> resultCoords) {
        // [minX, minY, maxX, maxY]
        double[] bbox = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};

        for (Point point : resultCoords) {
            if (bbox[0] > point.getLongitude()) { // minX
                bbox[0] = point.getLongitude();
            }
            if (bbox[1] > point.getLatitude()) { // minY
                bbox[1] = point.getLatitude();
            }
            if (bbox[2] < point.getLongitude()) { // maxX
                bbox[2] = point.getLongitude();
            }
            if (bbox[3] < point.getLatitude()) { // maxY
                bbox[3] = point.getLatitude();
            }
        }
        return BoundingBox.fromLngLats(bbox);
    }

    /**
     * 使用多边形所有顶点的平均值计算质心。
     *
     * @param polygon 多边形
     * @return 多边形质心
     */
    public static Point centroid(Polygon polygon) {
        double xSum = 0, ySum = 0, len = 0;
        for (Point p : polygon.coordinates()) {
            xSum += p.getLongitude();
            ySum += p.getLatitude();
            len++;
        }
        return Point.fromLngLat(xSum / len, ySum / len);
    }

    /**
     * 获取边框并计算包含输入的最小正方形边框
     *
     * @param bbox 边框
     * @return 正方形边框
     */
    public static BoundingBox square(BoundingBox bbox) {
        double[] b = bbox.bbox();

        double west = b[0], south = b[1], east = b[2], north = b[3];

        double horizontalDistance = distance(bbox.getSouthwest(), bbox.getSoutheast());
        double verticalDistance = distance(bbox.getNortheast(), bbox.getNorthwest());

        if (horizontalDistance >= verticalDistance) {
            double verticalMidpoint = (south + north) / 2;

            return BoundingBox.fromLngLats(west,
                    verticalMidpoint - (east - west) / 2,
                    east,
                    verticalMidpoint + (east - west) / 2);
        } else {
            double horizontalMidpoint = (west + east) / 2;

            return BoundingBox.fromLngLats(horizontalMidpoint - (north - south) / 2,
                    south,
                    horizontalMidpoint + (north - south) / 2,
                    north);
        }
    }

    /**
     * 返回沿该线指定公里数的点。
     *
     * @param line     线条图形
     * @param distance 指定的距离，单位公里
     * @return Point
     */
    public static Point along(Line line, double distance) {
        return along(line, distance, Units.KILOMETERS);
    }

    /**
     * 返回沿该线指定距离的点。
     *
     * @param line     线条图形
     * @param distance 距离
     * @param units    距离单位
     * @return Point
     */
    public static Point along(Line line, double distance, Units units) {
        List<Point> coords = line.coordinates();
        double travelled = 0;

        for (int i = 0, len = coords.size(); i < len; i++) {
            if (distance >= travelled && i == len - 1) {
                break;
            }

            Point currPoint = coords.get(i);
            if (travelled >= distance) {
                double overshot = distance - travelled;
                if (overshot == 0) {
                    return currPoint;
                } else {
                    double direction = bearing(currPoint, coords.get(i - 1)) - 180;
                    return destination(currPoint, overshot, direction, units);
                }
            } else {
                travelled += distance(currPoint, coords.get(i + 1), units);
            }
        }

        return coords.get(coords.size() - 1);
    }

    /**
     * 计算两点间的弧线<br>
     * 将大圆路径计算为 Line，默认返回100个点
     *
     * @param start   起始点
     * @param end     目标点
     * @return 返回弧线，可能为空，Line 或 MultiLine
     */
    public static Geometry greatCircle(Point start, Point end) {
        return greatCircle(start, end, 100, 0);
    }

    /**
     * 计算两点间的弧线<br>
     * 将大圆路径计算为 Line
     *
     * @param start   起始点
     * @param end     目标点
     * @param npoints 点的数量，如果小于等于0则默认为100
     * @param offset  偏移量控制了跨越日期线的线条被分割的概率。数值越高，可能性越大。如果小于等于0则默认为10
     * @return 返回弧线，可能为空，Line 或 MultiLine
     */
    public static Geometry greatCircle(Point start, Point end, int npoints, int offset) {
        if (npoints <= 0) {
            npoints = 100;
        }
        if (offset <= 0) {
            offset = 10;
        }

        return GreatCircle.arc(start, end, npoints, offset);
    }

}
