package com.atguigu.springcloud.test.tale.shape;

public final class Point implements CoordinateContainer<Point> {

    private final double longitude;

    private final double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static Point fromLngLat(double longitude, double latitude) {
        return new Point(longitude, latitude);
    }

    public static Point fromLngLat(double[] coords) {
        if (coords.length == 2) {
            return Point.fromLngLat(coords[0], coords[1]);
        }

        return null;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "Point{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public Point coordinates() {
        return this;
    }

    @Override
    public GeometryType type() {
        return GeometryType.POINT;
    }
}
