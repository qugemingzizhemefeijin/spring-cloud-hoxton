package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MultiPolygon implements Geometry<List<List<Point>>> {

    private final List<List<Point>> coordinates;

    MultiPolygon(List<List<Point>> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static MultiPolygon fromPolygons(List<Polygon> polygons) {
        List<List<Point>> coordinates = new ArrayList<>(polygons.size());
        for (Polygon polygon : polygons) {
            coordinates.add(polygon.coordinates());
        }
        return new MultiPolygon(coordinates);
    }

    public static MultiPolygon fromPolygon(Polygon polygon) {
        List<List<Point>> coordinates = Collections.singletonList(polygon.coordinates());
        return new MultiPolygon(coordinates);
    }

    public static MultiPolygon fromLngLats(List<List<Point>> points) {
        return new MultiPolygon(points);
    }

    public static MultiPolygon fromLngLats(double[][][] coordinates) {
        List<List<Point>> converted = new ArrayList<>(coordinates.length);

        for (double[][] coordinate : coordinates) {
            List<Point> innerOneList = new ArrayList<>(coordinate.length);
            for (double[] doubles : coordinate) {
                innerOneList.add(Point.fromLngLat(doubles));
            }
            converted.add(innerOneList);
        }

        return new MultiPolygon(converted);
    }

    public List<Polygon> polygons() {
        List<List<Point>> coordinates = coordinates();
        List<Polygon> polygons = new ArrayList<>(coordinates.size());
        for (List<Point> points : coordinates) {
            polygons.add(Polygon.fromLngLats(points));
        }
        return polygons;
    }

    @Override
    public List<List<Point>> coordinates() {
        return coordinates;
    }

    @Override
    public GeometryType type() {
        return GeometryType.MULTI_POLYGON;
    }

}
