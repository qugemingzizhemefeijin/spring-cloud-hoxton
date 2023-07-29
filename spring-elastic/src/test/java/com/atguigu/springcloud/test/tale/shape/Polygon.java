package com.atguigu.springcloud.test.tale.shape;

import com.atguigu.springcloud.test.tale.util.TaleHelper;

import java.util.ArrayList;
import java.util.List;

public final class Polygon implements CoordinateContainer<List<Point>, Polygon> {

    private List<Point> coordinates;

    Polygon(List<Point> coordinates) {
        if (coordinates == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates;
    }

    public static Polygon fromLngLats(List<Point> coordinates) {
        Point tailPt = coordinates.get(coordinates.size() - 1);
        Point headPt = coordinates.get(0);

        // 判断尾巴是否与头部相同
        boolean tail = TaleHelper.equals(headPt, tailPt);

        if (!tail) {
            coordinates.add(Point.fromLngLat(headPt));
        }

        return new Polygon(coordinates);
    }

    public static Polygon fromLngLats(double[][] coordinates) {
        double[] tailPt = coordinates[coordinates.length - 1];

        // 判断尾巴是否与头部相同
        boolean tail = coordinates[0][0] == tailPt[0] && coordinates[0][1] == tailPt[1];

        List<Point> converted = new ArrayList<>(tail ? coordinates.length : (coordinates.length + 1));
        for (double[] coordinate : coordinates) {
            converted.add(Point.fromLngLat(coordinate));
        }

        if (!tail) {
            converted.add(Point.fromLngLat(coordinates[0]));
        }

        return new Polygon(converted);
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
    public Polygon deepClone() {
        List<Point> list = new ArrayList<>(coordinates.size());
        for (Point point : coordinates) {
            list.add(point.deepClone());
        }
        return fromLngLats(list);
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
