package com.atguigu.springcloud.test.tale;

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

}
