package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleMeta;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;

import java.util.ArrayList;
import java.util.List;

public final class LineOffsetHelper {

    private LineOffsetHelper() {
        throw new AssertionError("No Instances.");
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends Geometry> T lineOffset(T geometry, double distance, Units units) {
        GeometryType type = geometry.type();
        switch (type) {
            case LINE:
                return (T) Line.fromLngLats(lineOffsetFeature(geometry, distance, units));
            case MULTI_LINE:
                List<List<Point>> coordinates = new ArrayList<>();
                TaleMeta.flattenEach(geometry, (g, multiIndex) -> {
                    coordinates.add(lineOffsetFeature(g, distance, units));

                    return true;
                });
                return (T) MultiLine.fromLngLats(coordinates);
            default:
                throw new TaleException("geometry " + type + " is not supported");
        }
    }

    private static <T extends Geometry> List<Point> lineOffsetFeature(T geometry, double distance, Units units) {
        List<Point> pointList = Line.line(geometry).coordinates();

        List<Point[]> segments = new ArrayList<>();
        double offsetDegrees = TaleHelper.lengthToDegrees(distance, units);
        List<Point> finalCoords = new ArrayList<>();

        for (int index = 0, size = pointList.size(); index < size; index++) {
            if (index != size - 1) {
                Point[] segment = processSegment(
                        pointList.get(index),
                        pointList.get(index + 1),
                        offsetDegrees
                );
                segments.add(segment);

                if (index > 0) {
                    Point[] seg2Coords = segments.get(index - 1);
                    Point intersects = intersection(segment, seg2Coords);

                    // Handling for line segments that aren't straight
                    if (intersects != null) {
                        seg2Coords[1] = intersects;
                        segment[0] = intersects;
                    }

                    finalCoords.add(seg2Coords[0]);
                    if (index == size - 2) {
                        finalCoords.add(segment[0]);
                        finalCoords.add(segment[1]);
                    }
                }

                // Handling for lines that only have 1 segment
                if (size == 2) {
                    finalCoords.add(segment[0]);
                    finalCoords.add(segment[1]);
                }
            }
        }

        return finalCoords;
    }

    private static Point intersection(Point[] a, Point[] b) {
        if (isParallel(a, b)) {
            return null;
        }

        return intersectSegments(a, b);
    }

    private static boolean isParallel(Point[] a, Point[] b) {
        double[] r = ab(a);
        double[] s = ab(b);
        return crossProduct(r, s) == 0;
    }

    private static double[] ab(Point[] segment) {
        Point start = segment[0];
        Point end = segment[1];
        return new double[]{end.getX() - start.getX(), end.getY() - start.getY()};
    }

    private static double[] sub(double[] v1, double[] v2) {
        return new double[]{v1[0] - v2[0], v1[1] - v2[1]};
    }

    private static double[] add(double[] v1, double[] v2) {
        return new double[]{v1[0] + v2[0], v1[1] + v2[1]};
    }

    private static double[] scalarMult(double s, double[] v) {
        return new double[]{s * v[0], s * v[1]};
    }

    private static double crossProduct(double[] v1, double[] v2) {
        return v1[0] * v2[1] - v2[0] * v1[1];
    }

    private static Point intersectSegments(Point[] a, Point[] b) {
        double[] p = a[0].getCoord();
        double[] r = ab(a);
        double[] q = b[0].getCoord();
        double[] s = ab(b);

        double cross = crossProduct(r, s);
        double[] qmp = sub(q, p);
        double numerator = crossProduct(qmp, s);
        double t = numerator / cross;
        double[] intersection = add(p, scalarMult(t, r));

        return Point.fromLngLat(intersection);
    }

    /**
     * Process Segment
     * <p>
     * Inspiration taken from http://stackoverflow.com/questions/2825412/draw-a-parallel-line
     *
     * @param point1 点坐标1
     * @param point2 点坐标2
     * @param offset 偏移度数
     * @return 计算好的偏移坐标点
     */
    private static Point[] processSegment(Point point1, Point point2, double offset) {
        double x1 = point1.getX(), y1 = point1.getY(), x2 = point2.getX(), y2 = point2.getY();

        double L = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        double out1x = x1 + (offset * (y2 - y1)) / L;
        double out2x = x2 + (offset * (y2 - y1)) / L;
        double out1y = y1 + (offset * (x1 - x2)) / L;
        double out2y = y2 + (offset * (x1 - x2)) / L;

        return new Point[]{Point.fromLngLat(out1x, out1y), Point.fromLngLat(out2x, out2y)};
    }

}
