package com.atguigu.springcloud.test.tale.shape;

import java.io.Serializable;

public final class BoundingBox implements Serializable {

    private static final long serialVersionUID = 1870316096198598853L;

    /**
     * 西南点
     */
    private final Point southwest;

    /**
     * 东北点
     */
    private final Point northeast;

    BoundingBox(Point southwest, Point northeast) {
        if (southwest == null) {
            throw new NullPointerException("Null southwest");
        }
        this.southwest = southwest;
        if (northeast == null) {
            throw new NullPointerException("Null northeast");
        }
        this.northeast = northeast;
    }

    public static BoundingBox fromPoints(Point southwest, Point northeast) {
        return new BoundingBox(southwest, northeast);
    }

    public static BoundingBox fromLngLats(double west, double south, double east, double north) {
        return new BoundingBox(Point.fromLngLat(west, south), Point.fromLngLat(east, north));
    }

    public static BoundingBox fromLngLats(double[] bbox) {
        if (bbox == null || bbox.length != 4) {
            throw new IllegalArgumentException("bbox can not null, and length not eq 4");
        }

        return new BoundingBox(Point.fromLngLat(bbox[0], bbox[1]), Point.fromLngLat(bbox[2], bbox[3]));
    }

    public Point getSouthwest() {
        return southwest;
    }

    public Point getNortheast() {
        return northeast;
    }

    public Point getSoutheast() {
        return Point.fromLngLat(east(), south());
    }

    public Point getNorthwest() {
        return Point.fromLngLat(west(), north());
    }

    public final double west() {
        return southwest.getLongitude();
    }

    public final double south() {
        return southwest.getLatitude();
    }

    public final double east() {
        return northeast.getLongitude();
    }

    public final double north() {
        return northeast.getLatitude();
    }

    public final double[] bbox() {
        return new double[]{southwest.getLongitude(), southwest.getLatitude(), northeast.getLongitude(), northeast.getLatitude()};
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "southwest=" + southwest +
                ", northeast=" + northeast +
                '}';
    }
}
