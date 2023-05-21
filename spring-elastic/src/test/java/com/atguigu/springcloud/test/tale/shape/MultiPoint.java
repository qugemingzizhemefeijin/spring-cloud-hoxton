package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class MultiPoint implements CoordinateContainer<List<Point>> {

    private final List<Point> coordinates;

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

    @Override
    public List<Point> coordinates() {
        return coordinates;
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
