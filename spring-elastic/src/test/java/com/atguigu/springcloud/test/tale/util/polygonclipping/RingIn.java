package com.atguigu.springcloud.test.tale.util.polygonclipping;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.ArrayList;
import java.util.List;

public class RingIn {

    private PolyIn poly;
    private boolean isExterior;
    private List<Segment> segments;

    private PtRounder rounder;
    private Bbox bbox;

    public RingIn(List<Point> geomRing, PolyIn poly, boolean isExterior) {
        if (geomRing == null || geomRing.isEmpty()) {
            throw new TaleException("Input geometry is not a valid Polygon or MultiPolygon");
        }

        this.poly = poly;
        this.isExterior = isExterior;
        this.segments = new ArrayList<>();

        this.rounder = new PtRounder();

        Location firstPoint = rounder.round(geomRing.get(0).getX(), geomRing.get(0).getY());
        this.bbox = new Bbox(Location.location(firstPoint.getX(), firstPoint.getY()), Location.location(firstPoint.getX(), firstPoint.getY()));

        Location prevPoint = firstPoint;
        for (int i = 1, iMax = geomRing.size(); i < iMax; i++) {
            Location point = rounder.round(geomRing.get(i).getX(), geomRing.get(i).getY());
            // skip repeated points
            if (point.getX() == prevPoint.getX() && point.getY() == prevPoint.getY()) {
                continue;
            }
            this.segments.add(Segment.fromRing(prevPoint, point, this));
            if (point.getX() < this.bbox.ll.getX()) {
                this.bbox.ll.setX(point.getX());
            }
            if (point.getY() < this.bbox.ll.getY()) {
                this.bbox.ll.setY(point.getY());
            }
            if (point.getX() > this.bbox.ur.getX()) {
                this.bbox.ur.setX(point.getX());
            }
            if (point.getY() > this.bbox.ur.getY()) {
                this.bbox.ur.setY(point.getY());
            }
            prevPoint = point;
        }

        // add segment from last to first if last is not the same as first
        if (firstPoint.getX() != prevPoint.getX() || firstPoint.getY() != prevPoint.getY()) {
            this.segments.add(Segment.fromRing(prevPoint, firstPoint, this));
        }
    }

    public List<SweepEvent> getSweepEvents() {
        int iMax = this.segments.size();
        List<SweepEvent> sweepEvents = new ArrayList<>(iMax);
        for (int i = 0; i < iMax; i++) {
            Segment segment = this.segments.get(i);
            sweepEvents.add(segment.leftSE);
            sweepEvents.add(segment.rightSE);
        }
        return sweepEvents;
    }

    public PolyIn getPoly() {
        return poly;
    }

    public boolean isExterior() {
        return isExterior;
    }

    public Bbox getBbox() {
        return bbox;
    }
}
