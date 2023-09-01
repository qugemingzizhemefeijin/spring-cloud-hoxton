package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.ArrayList;
import java.util.List;

public class MultiPolyIn {

    private List<PolyIn> polys;
    private Bbox bbox;
    private boolean isSubject;

    public MultiPolyIn(List<List<Point>> geom, boolean isSubject) {
        if (geom == null || geom.isEmpty()) {
            throw new TaleException("Input geometry is not a valid Polygon or MultiPolygon");
        }

        this.polys = new ArrayList<>(1);
        this.bbox = new Bbox(
                Location.location(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                Location.location(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        );

        PolyIn poly = new PolyIn(geom, this);
        if (poly.getBbox().ll.getX() < this.bbox.ll.getX()) {
            this.bbox.ll.setX(poly.getBbox().ll.getX());
        }
        if (poly.getBbox().ll.getY() < this.bbox.ll.getY()) {
            this.bbox.ll.setY(poly.getBbox().ll.getY());
        }
        if (poly.getBbox().ur.getX() > this.bbox.ur.getX()) {
            this.bbox.ur.setX(poly.getBbox().ur.getX());
        }
        if (poly.getBbox().ur.getY() > this.bbox.ur.getY()) {
            this.bbox.ur.setY(poly.getBbox().ur.getY());
        }
        this.polys.add(poly);

        this.isSubject = isSubject;
    }

    public List<SweepEvent> getSweepEvents() {
        List<SweepEvent> sweepEvents = new ArrayList<>();
        for (PolyIn poly : this.polys) {
            sweepEvents.addAll(poly.getSweepEvents());
        }
        return sweepEvents;
    }

}
