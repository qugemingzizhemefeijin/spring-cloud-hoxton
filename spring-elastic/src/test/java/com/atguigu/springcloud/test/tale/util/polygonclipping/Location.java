package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.shape.Point;

public final class Location {

    private double x;

    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Location(Point p) {
        this(p.getX(), p.getY());
    }

    public static Location location(double x, double y) {
        return new Location(x, y);
    }

    public static Location location(Point p) {
        return new Location(p);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
