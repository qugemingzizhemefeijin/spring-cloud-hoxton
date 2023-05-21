package com.atguigu.springcloud.test.tale.shape;

import java.util.ArrayList;
import java.util.List;

public final class GeometryCollection implements Geometry {

    private final List<Geometry> geometries;

    GeometryCollection(List<Geometry> geometries) {
        if (geometries == null) {
            throw new NullPointerException("Null geometries");
        }
        this.geometries = geometries;
    }

    public static GeometryCollection fromGeometries(List<Geometry> geometries) {
        return new GeometryCollection(geometries);
    }

    public static GeometryCollection fromGeometry(Geometry geometry) {
        List<Geometry> geometries = new ArrayList<>(1);
        geometries.add(geometry);

        return new GeometryCollection(geometries);
    }

    public List<Geometry> geometries() {
        return geometries;
    }

    @Override
    public GeometryType type() {
        return GeometryType.GEOMETRY_COLLECTION;
    }

    @Override
    public String toString() {
        return "GeometryCollection{" +
                "geometries=" + geometries +
                '}';
    }
}
