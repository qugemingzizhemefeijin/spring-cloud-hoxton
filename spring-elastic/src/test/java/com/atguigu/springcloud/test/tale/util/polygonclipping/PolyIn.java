package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.ArrayList;
import java.util.List;

public class PolyIn {

    private RingIn exteriorRing;
    private Bbox bbox;
    private List<RingIn> interiorRings;
    private MultiPolyIn multiPoly;

    public PolyIn(List<List<Point>> geomPoly, MultiPolyIn multiPoly) {
        if (geomPoly == null || geomPoly.isEmpty()) {
            throw new TaleException("Input geometry is not a valid Polygon or MultiPolygon");
        }

        this.exteriorRing = new RingIn(geomPoly.get(0), this, true);
        // copy by value
        this.bbox = new Bbox(
                Location.location(this.exteriorRing.getBbox().ll.getX(), this.exteriorRing.getBbox().ll.getY()),
                Location.location(this.exteriorRing.getBbox().ur.getX(), this.exteriorRing.getBbox().ur.getY())
        );

        this.interiorRings = new ArrayList<>();
        for (int i = 1, iMax = geomPoly.size(); i < iMax; i++) {
            RingIn ring = new RingIn(geomPoly.get(i), this, false);
            if (ring.getBbox().ll.getX() < this.bbox.ll.getX()) {
                this.bbox.ll.setX(ring.getBbox().ll.getX());
            }
            if (ring.getBbox().ll.getY() < this.bbox.ll.getY()) {
                this.bbox.ll.setY(ring.getBbox().ll.getY());
            }
            if (ring.getBbox().ur.getX() > this.bbox.ur.getX()) {
                this.bbox.ur.setX(ring.getBbox().ur.getX());
            }
            if (ring.getBbox().ur.getY() > this.bbox.ur.getY()) {
                this.bbox.ur.setY(ring.getBbox().ur.getY());
            }
            this.interiorRings.add(ring);
        }
        this.multiPoly = multiPoly;
    }

    public List<SweepEvent> getSweepEvents() {
        List<SweepEvent> sweepEvents = this.exteriorRing.getSweepEvents();
        for (RingIn interiorRing : this.interiorRings) {
            sweepEvents.addAll(interiorRing.getSweepEvents());
        }
        return sweepEvents;
    }

    public Bbox getBbox() {
        return bbox;
    }
}
