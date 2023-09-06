package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleMeasurement;
import com.atguigu.springcloud.test.tale.TaleMeta;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.buffer.BufferOp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class BufferHelper {

    private BufferHelper() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 计算缓冲区单个组件
     *
     * @param geometry 要计算缓存区的组件
     * @param radius   绘制缓冲区的距离（允许负值）
     * @param units    距离单位
     * @param steps    频数
     * @return 计算后的组件
     */
    public static Geometry buffer(Geometry geometry, int radius, Units units, int steps) {
        if (geometry.type() == GeometryType.GEOMETRY_COLLECTION) {
            List<Geometry> results = new ArrayList<>();
            TaleMeta.geomEach(geometry, (g, parent, geomIndex) -> {
                Geometry buffered = buffer(g, radius, units, steps);
                if (buffered != null) {
                    results.add(buffered);
                }
                return true;
            });

            return results.isEmpty() ? null : GeometryCollection.fromGeometries(results);
        }

        // Project GeoJSON to Azimuthal Equidistant projection (convert to Meters)

        // JSTS buffer operation
        double distance = TaleHelper.radiansToLength(TaleHelper.lengthToRadians(radius, units), Units.METERS);

        // 这里可以直接做一层中转，以后可以实现GeoJSON，就不需要这么麻烦咯。。。
        com.vividsolutions.jts.geom.Geometry buffered = BufferOp.bufferOp(transformation(geometry), distance, steps);

        // Detect if empty geometries
        if (coordsIsNaN(buffered.getCoordinates())) {
            return null;
        }

        // Unproject coordinates (convert to Degrees)
        return unTransformation(buffered);
    }

    private static Geometry unTransformation(com.vividsolutions.jts.geom.Geometry geometry) {
        return null;
    }

    /**
     * 将 Geometry 转成对应的 JTS的Geometry
     *
     * @param geometry 要转换的组件
     * @return JTS Geometry 组件
     */
    private static com.vividsolutions.jts.geom.Geometry transformation(Geometry geometry) {
        GeometryFactory geometryFactory = new GeometryFactory();

        GeometryType type = geometry.type();
        switch (type) {
            case POINT: {
                Point p = Point.point(geometry);
                return geometryFactory.createPoint(new Coordinate(p.getX(), p.getY()));
            }
            case MULTI_POINT: {
                MultiPoint multiPoint = MultiPoint.multiPoint(geometry);
                return geometryFactory.createMultiPoint(multiPoint.coordinates().stream().map(p -> new Coordinate(p.getX(), p.getY())).toArray(Coordinate[]::new));
            }
            case LINE: {
                Line line = Line.line(geometry);
                return geometryFactory.createLineString(line.coordinates().stream().map(p -> new Coordinate(p.getX(), p.getY())).toArray(Coordinate[]::new));
            }
            case MULTI_LINE: {
                MultiLine multiLine = MultiLine.multiLine(geometry);
                return geometryFactory.createMultiLineString(
                        multiLine.coordinates()
                                .stream()
                                .map(pointList -> geometryFactory.createLineString(pointList.stream().map(p -> new Coordinate(p.getX(), p.getY())).toArray(Coordinate[]::new)))
                                .toArray(LineString[]::new)
                );
            }
            case POLYGON: {
                Polygon polygon = Polygon.polygon(geometry);
                return geometryFactory.createPolygon(polygon.coordinates().stream().map(p -> new Coordinate(p.getX(), p.getY())).toArray(Coordinate[]::new));
            }
            case MULTI_POLYGON: {
                MultiPolygon multiPolygon = MultiPolygon.multiPolygon(geometry);
                return geometryFactory.createMultiPolygon(
                        multiPolygon.coordinates()
                                .stream()
                                .map(pointList -> geometryFactory.createPolygon(pointList.stream().map(p -> new Coordinate(p.getX(), p.getY())).toArray(Coordinate[]::new)))
                                .toArray(com.vividsolutions.jts.geom.Polygon[]::new)
                );
            }
            default:
                throw new TaleException(type + " geometry not supported");
        }
    }

    public static void main(String[] args) {
        Point p = Point.fromLngLat(1, 2);
        double distance = TaleHelper.radiansToLength(TaleHelper.lengthToRadians(500, Units.KILOMETERS), Units.METERS);

        GeometryFactory geometryFactory = new GeometryFactory();
        com.vividsolutions.jts.geom.Point v = geometryFactory.createPoint(new Coordinate(1, 2));
        com.vividsolutions.jts.geom.Geometry vv = BufferOp.bufferOp(v, distance, 8);
        System.out.println(vv.toText());
    }

    /**
     * 判断坐标中的值是否是 NaN
     *
     * @param coords 坐标集合
     * @return 如果 NaN 存在返回true，否则false
     */
    private static boolean coordsIsNaN(Coordinate[] coords) {
        if (coords == null || coords.length == 0) {
            return false;
        }
        for (Coordinate coord : coords) {
            if (Double.isNaN(coord.x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 投影坐标
     *
     * @param coords 要计算的坐标
     * @param proj   D3地理投影
     * @return 投影后的坐标
     */
    private static double[] projectCoords(Point coords, String proj) {
        return null;//proj(coords);
    }

    /**
     * 投影坐标
     *
     * @param coords 要计算的坐标集合
     * @param proj   D3地理投影
     * @return 投影后的坐标集合
     */
    private static List<double[]> projectCoords(List<Point> coords, String proj) {
        return coords.stream().map(p -> projectCoords(p, proj)).collect(Collectors.toList());
    }

    /**
     * 投影坐标
     *
     * @param coords 要计算的坐标集合
     * @param proj   D3地理投影
     * @return 投影后的坐标集合
     */
    private static List<List<double[]>> projectMultiCoords(List<List<Point>> coords, String proj) {
        return coords.stream().map(plist -> projectCoords(plist, proj)).collect(Collectors.toList());
    }

    /**
     * 将坐标取消投影
     *
     * @param coords 要取消的坐标
     * @param proj   D3地理投影
     * @return 取消后的坐标
     */
    private static double[] unprojectCoords(Point coords, String proj) {
        return null;//proj.invert(coords);
    }

    /**
     * 将坐标取消投影
     *
     * @param coords 要取消的坐标集合
     * @param proj   D3地理投影
     * @return 取消后的坐标集合
     */
    private static List<double[]> unprojectCoords(List<Point> coords, String proj) {
        return coords.stream().map(p -> unprojectCoords(p, proj)).collect(Collectors.toList());
    }

    /**
     * 将坐标取消投影
     *
     * @param coords 要取消的坐标集合
     * @param proj   D3地理投影
     * @return 取消后的坐标集合
     */
    private static List<List<double[]>> unprojectMultiCoords(List<List<Point>> coords, String proj) {
        return coords.stream().map(plist -> unprojectCoords(plist, proj)).collect(Collectors.toList());
    }

    /**
     * 定义方位等距投影
     *
     * @param geometry 图形组件
     * @return D3 地理方位等距投影
     */
    private static double defineProjection(Geometry geometry) {
        Point coords = TaleMeasurement.center(geometry);
        double[] rotation = new double[]{-coords.getX(), -coords.getY()};

        return 0;//geoAzimuthalEquidistant().rotate(rotation).scale(TaleHelper.EARTH_RADIUS);
    }

}
