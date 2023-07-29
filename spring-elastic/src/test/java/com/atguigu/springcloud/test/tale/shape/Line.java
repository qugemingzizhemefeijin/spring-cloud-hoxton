package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class Line implements CoordinateContainer<List<Point>, Line> {

    private List<Point> coordinates;

    Line(List<Point> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static Line fromLngLats(List<Point> points) {
        return new Line(points);
    }

    public static Line fromLngLatsShallowCopy(List<Point> points) {
        return new Line(new ArrayList<>(points));
    }

    public static Line fromLngLats(Point start, Point end) {
        List<Point> points = new ArrayList<>(2);
        points.add(start);
        points.add(end);

        return fromLngLats(points);
    }

    public static Line fromLngLats(double x1, double y1, double x2, double y2) {
        return fromLngLats(Point.fromLngLat(x1, y1), Point.fromLngLat(x2, y2));
    }

    public static Line fromLngLats(double[][] coordinates) {
        List<Point> points = new ArrayList<>(coordinates.length);

        for (double[] p : coordinates) {
            points.add(Point.fromLngLat(p));
        }

        return fromLngLats(points);
    }

    public static Line line(Geometry g) {
        return (Line)g;
    }

    @Override
    public List<Point> coordinates() {
        return coordinates;
    }

    @Override
    public int coordsSize() {
        return coordinates != null ? coordinates.size() : 0;
    }

    @Override
    public void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
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
