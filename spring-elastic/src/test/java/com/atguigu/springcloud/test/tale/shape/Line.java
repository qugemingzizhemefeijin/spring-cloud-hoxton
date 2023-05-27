package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class Line implements CoordinateContainer<List<Point>, Line> {

    private final List<Point> coordinates;

    Line(List<Point> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static Line fromLngLats(List<Point> points) {
        return new Line(points);
    }

    public static Line fromLngLats(Point start, Point end) {
        List<Point> points = new ArrayList<>(2);
        points.add(start);
        points.add(end);

        return fromLngLats(points);
    }

    public static Line fromLngLats(double[][] coordinates) {
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
    public Line deepClone() {
        List<Point> list = new ArrayList<>(coordinates.size());
        for (Point point : coordinates) {
            list.add(point.deepClone());
        }
        return fromLngLats(list);
    }

    @Override
    public GeometryType type() {
        return GeometryType.LINE;
    }

    @Override
    public String toString() {
        return "Line{" +
                "coordinates=" + coordinates +
                '}';
    }
}
