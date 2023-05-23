package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.callback.CoordEachCallback;
import com.atguigu.springcloud.test.tale.shape.*;

import java.util.ArrayList;
import java.util.List;

public final class TaleMeta {

    private TaleMeta() {
        throw new AssertionError("No Instances.");
    }

    public static List<Point> coordAll(Line line) {
        return coordAll(new ArrayList<>(), line);
    }

    private static List<Point> coordAll(List<Point> coords, Line line) {
        coords.addAll(line.coordinates());
        return coords;
    }

    public static List<Point> coordAll(Polygon polygon, boolean excludeWrapCoord) {
        return coordAll(new ArrayList<>(), polygon, excludeWrapCoord);
    }

    private static List<Point> coordAll(List<Point> coords,
                                        Polygon polygon,
                                        boolean excludeWrapCoord) {
        int wrapShrink = excludeWrapCoord ? 1 : 0;
        List<Point> coordinates = polygon.coordinates();

        for (int i = 0, size = coordinates.size(); i < size - wrapShrink; i++) {
            coords.add(coordinates.get(i));
        }
        return coords;
    }

    public static List<Point> coordAll(MultiPolygon multiPolygon,
                                       boolean excludeWrapCoord) {
        return coordAll(new ArrayList<>(), multiPolygon, excludeWrapCoord);
    }

    private static List<Point> coordAll(List<Point> coords,
                                        MultiPolygon multiPolygon,
                                        boolean excludeWrapCoord) {
        int wrapShrink = excludeWrapCoord ? 1 : 0;

        for (List<Point> coordinate : multiPolygon.coordinates()) {
            for (int i = 0, size = coordinate.size(); i < size - wrapShrink; i++) {
                coords.add(coordinate.get(i));
            }
        }
        return coords;
    }

    public static List<Point> coordAll(MultiLine multiLine) {
        return coordAll(new ArrayList<>(), multiLine);
    }

    private static List<Point> coordAll(List<Point> coords, MultiLine multiLine) {
        for (List<Point> coordinate : multiLine.coordinates()) {
            coords.addAll(coordinate);
        }
        return coords;
    }

    public static List<Point> coordAll(MultiPoint multiPoint) {
        return coordAll(new ArrayList<>(), multiPoint);
    }

    private static List<Point> coordAll(List<Point> coords, MultiPoint multiPoint) {
        coords.addAll(multiPoint.coordinates());
        return coords;
    }

    public static List<Point> coordAll(GeometryCollection geometryCollection, boolean excludeWrapCoord) {
        return coordAllFromGeometry(new ArrayList<>(), geometryCollection, excludeWrapCoord);
    }

    private static List<Point> coordAllFromGeometry(List<Point> pointList,
                                                    Geometry geometry,
                                                    boolean excludeWrapCoord) {
        switch (geometry.type()) {
            case POINT:
                pointList.add((Point) geometry);
                break;
            case LINE:
                pointList.addAll(((Line) geometry).coordinates());
                break;
            case POLYGON:
                coordAll(pointList, (Polygon) geometry, excludeWrapCoord);
                break;
            case MULTI_LINE:
                coordAll(pointList, (MultiLine) geometry);
                break;
            case MULTI_POLYGON:
                coordAll(pointList, (MultiPolygon) geometry, excludeWrapCoord);
                break;
            case MULTI_POINT:
                coordAll(pointList, (MultiPoint) geometry);
                break;
            case GEOMETRY_COLLECTION:
                for (Geometry singleGeometry : ((GeometryCollection) geometry).geometries()) {
                    coordAllFromGeometry(pointList, singleGeometry, excludeWrapCoord);
                }
                break;
        }
        return pointList;
    }

    /**
     * 循环处理组件点信息
     * @param geometry 图形组件
     * @param callback 处理函数
     * @return 是否所有的点均处理成功
     */
    public static <T extends Geometry> boolean coordEach(T geometry, CoordEachCallback callback) {
        return coordEach(geometry, callback, false);
    }

    /**
     * 循环处理组件点信息
     * @param geometry         图形组件
     * @param callback         处理函数
     * @param excludeWrapCoord 如果是 POLYGON || MULTI_POLYGON 是否处理最后一个闭合点
     * @return 是否所有的点均处理成功
     */
    @SuppressWarnings({"all"})
    public static <T extends Geometry> boolean coordEach(T geometry, CoordEachCallback callback, boolean excludeWrapCoord) {
        if (geometry == null) {
            return false;
        }

        GeometryType geometryType = geometry.type();
        GeometryCollection geometryCollection = geometryType == GeometryType.GEOMETRY_COLLECTION ? ((GeometryCollection) geometry) : null;
        int stop = geometryCollection != null ? geometryCollection.geometries().size() : 1;

        for (int geomIndex = 0; geomIndex < stop; geomIndex++) {
            int multiIndex = 0;
            Geometry g = geometryCollection != null ? geometryCollection.geometries().get(geomIndex) : geometry;
            GeometryType gType = g.type();
            int wrapShrink = excludeWrapCoord && (gType == GeometryType.POLYGON || gType == GeometryType.MULTI_POLYGON) ? 1 : 0;

            if (gType == GeometryType.POINT) {
                if (!callback.accept((Point) geometry, 0, 0, geomIndex)) {
                    return false;
                }
            } else if (gType == GeometryType.LINE || gType == GeometryType.POLYGON || gType == GeometryType.MULTI_POINT) {
                List<Point> coordinate = ((CoordinateContainer<List<Point>>) geometry).coordinates();
                for (int i = 0, size = coordinate.size() - wrapShrink; i < size; i++) {
                    if (!callback.accept(coordinate.get(i), i, 0, geomIndex)) {
                        return false;
                    }
                }
            } else if (gType == GeometryType.MULTI_LINE || gType == GeometryType.MULTI_POLYGON) {
                List<List<Point>> coordinates = ((CoordinateContainer<List<List<Point>>>) geometry).coordinates();
                for (int i = 0, isize = coordinates.size() - wrapShrink; i < isize; i++) {
                    List<Point> coordinate = coordinates.get(i);
                    for (int j = 0, jsize = coordinate.size(); j < jsize; j++) {
                        if (!callback.accept(coordinate.get(j), j, i, geomIndex)) {
                            return false;
                        }
                    }
                }
            } else if (gType == GeometryType.GEOMETRY_COLLECTION) {
                GeometryCollection newGeometryCollection = (GeometryCollection) g;
                for (Geometry newGeometry : newGeometryCollection.geometries()) {
                    if (!coordEach(newGeometry, callback, excludeWrapCoord)) {
                        return false;
                    }
                }
            } else {
                throw new IllegalArgumentException("Unknown Geometry Type");
            }
        }

        return true;
    }

}
