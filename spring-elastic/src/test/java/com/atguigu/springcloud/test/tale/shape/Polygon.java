package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class Polygon implements CoordinateContainer<List<Point>> {

    private final List<Point> coordinates;

    Polygon(List<Point> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static Polygon fromLngLats(List<Point> coordinates) {
        return new Polygon(coordinates);
    }

    public static Polygon fromLngLats(double[][] coordinates) {
        List<Point> converted = new ArrayList<>(coordinates.length);
        for (double[] coordinate : coordinates) {
            converted.add(Point.fromLngLat(coordinate));
        }
        return new Polygon(converted);
    }

    @Override
    public List<Point> coordinates() {
        return coordinates;
    }

    @Override
    public GeometryType type() {
        return GeometryType.POLYGON;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "coordinates=" + coordinates +
                '}';
    }
}
