package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class MultiPoint implements CoordinateContainer<List<Point>, MultiPoint> {

    private List<Point> coordinates;

    MultiPoint(List<Point> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static MultiPoint fromLngLats(List<Point> points) {
        return new MultiPoint(points);
    }

    public static MultiPoint fromLngLats(double[][] coordinates) {
        List<Point> points = new ArrayList<>(coordinates.length);

        for (double[] p : coordinates) {
            points.add(Point.fromLngLat(p));
        }

        return fromLngLats(points);
    }

    public static MultiPoint multiPoint(Geometry g) {
        return (MultiPoint)g;
    }

    @Override
    public List<Point> coordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int coordsSize() {
        return coordinates != null ? coordinates.size() : 0;
    }

    @Override
    public MultiPoint deepClone() {
        List<Point> list = new ArrayList<>(coordinates.size());
        for (Point point : coordinates) {
            list.add(point.deepClone());
        }
        return fromLngLats(list);
    }

    @Override
    public GeometryType type() {
        return GeometryType.MULTI_POINT;
    }

    @Override
    public String toString() {
        return "MultiPoint{" +
                "coordinates=" + coordinates +
                '}';
    }
}
