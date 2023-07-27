package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.models.IntersectsResult;
import com.atguigu.springcloud.test.tale.shape.*;

import java.util.ArrayList;
import java.util.List;

public final class TaleMisc {

    private TaleMisc() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 返回自身的相交点
     *
     * @param geometry 图形，支持 POLYGON、LINE、MULTI_LINE、MULTI_POLYGON
     * @return 返回自相交点集合
     */
    public static List<Point> kinks(Geometry geometry) {
        switch(geometry.type()) {
            case POLYGON:
                return kinks(((Polygon)geometry).coordinates());
            case LINE:
                return kinks(((Line)geometry).coordinates());
            case MULTI_LINE:
                return kinksMulti(((MultiLine)geometry).coordinates());
            case MULTI_POLYGON:
                return kinksMulti(((MultiPolygon)geometry).coordinates());
        }
        return null;
    }

    private static List<Point> kinksMulti(List<List<Point>> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return null;
        }

        List<Point> pointList = new ArrayList<>();
        for (List<Point> c : coordinates) {
            if (c == null || c.isEmpty()) {
                continue;
            }

            kinks(c, pointList);
        }

        return pointList;
    }

    private static List<Point> kinks(List<Point> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return null;
        }

        List<Point> pointList = new ArrayList<>();

        kinks(coordinates, pointList);

        return pointList;
    }

    private static void kinks(List<Point> coordinates, List<Point> pointList) {
        int len = coordinates.size();
        for (int i = 0; i < len - 1; i++) {
            for (int k = i; k < len - 1; k++) {
                // segments are adjacent and always share a vertex, not a kink
                if (Math.abs(i - k) == 1) {
                    continue;
                }
                // first and last segment in a closed lineString or ring always share a vertex, not a kink
                if (i == 0 && k == len - 2 && coordinates.get(i).getLongitude() == coordinates.get(len - 1).getLongitude() && coordinates.get(i).getLatitude() == coordinates.get(len - 1).getLatitude()) {
                    continue;
                }

                IntersectsResult result = lineIntersects(coordinates.get(i), coordinates.get(i + 1), coordinates.get(k), coordinates.get(k + 1));
                if (result != null) {
                    pointList.add(Point.fromLngLat(result.getX(), result.getY()));
                }
            }
        }
    }

    private static IntersectsResult lineIntersects(Point line1Start, Point line1End, Point line2Start, Point line2End) {
        return lineIntersects(line1Start.getLongitude(), line1Start.getLatitude(),
                line1End.getLongitude(), line1End.getLatitude(),
                line2Start.getLongitude(), line2Start.getLatitude(),
                line2End.getLongitude(), line2End.getLatitude());
    }

    private static IntersectsResult lineIntersects(double line1StartX, double line1StartY,
                                                  double line1EndX, double line1EndY,
                                                  double line2StartX, double line2StartY,
                                                  double line2EndX, double line2EndY) {
        double denominator, a, b, numerator1, numerator2;
        IntersectsResult result = new IntersectsResult();
        denominator = (line2EndY - line2StartY) * (line1EndX - line1StartX) - (line2EndX - line2StartX) * (line1EndY - line1StartY);
        if (denominator == 0) {
            if (result.getX() != null && result.getY() != null) {
                return result;
            } else {
                return null;
            }
        }

        a = line1StartY - line2StartY;
        b = line1StartX - line2StartX;
        numerator1 = (line2EndX - line2StartX) * a - (line2EndY - line2StartY) * b;
        numerator2 = (line1EndX - line1StartX) * a - (line1EndY - line1StartY) * b;
        a = numerator1 / denominator;
        b = numerator2 / denominator;

        result.setX(line1StartX + a * (line1EndX - line1StartX));
        result.setY(line1StartY + a * (line1EndY - line1StartY));

        if (a >= 0 && a <= 1) {
            result.setOnLine1(true);
        }
        if (b >= 0 && b <= 1) {
            result.setOnLine2(true);
        }

        if (result.isOnLine1() && result.isOnLine2()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 多边型顶点连线，从一个(多)Line或(多)Polygon创建一个2-vertex线段的GeometryCollection
     * @param geometry 支持 Line|MultiLine|MultiPolygon|Polygon
     * @return List<Line>
     */
    public static List<Line> lineSegment(Geometry geometry) {
        if (geometry == null) {
            throw new TaleException("geometry is required");
        }

        List<Line> results = new ArrayList<>();
        TaleMeta.flattenEach(geometry, (g, multiIndex) -> {
            lineSegmentFeature(g, results);
            return true;
        });

        return results;
    }

    /**
     * 从Line中创建线段
     * @param geometry 支持 Line|Polygon
     * @param results  待返回的线段集合
     */
    private static void lineSegmentFeature(Geometry geometry, List<Line> results) {
        GeometryType type = geometry.type();
        List<Point> coords = null;
        switch (type) {
            case POLYGON:
                coords = ((Polygon)geometry).coordinates();
                break;
            case LINE:
                coords = ((Line)geometry).coordinates();
                break;
        }

        if (coords == null || coords.isEmpty()) {
            return;
        }

        // 组合成N组线段
        List<Line> segments = createSegments(coords);
        if (!segments.isEmpty()) {
            results.addAll(segments);
        }
    }

    /**
     * 将传入的coords中的点两两组合成一组线段
     * @param coords 传入的坐标组
     * @return List<Line>
     */
    private static List<Line> createSegments(List<Point> coords) {
        List<Line> segments = new ArrayList<>();

        for (int i = 1, size = coords.size(); i < size; i++) {
            Point previousCoords = coords.get(i - 1);
            Point currentCoords = coords.get(i);

            segments.add(Line.fromLngLats(previousCoords, currentCoords));
        }

        return segments;
    }

}
