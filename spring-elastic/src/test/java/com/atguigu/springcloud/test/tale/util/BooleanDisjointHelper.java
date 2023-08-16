package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleBooleans;
import com.atguigu.springcloud.test.tale.TaleFeatureConversion;
import com.atguigu.springcloud.test.tale.TaleMisc;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;

import java.util.List;

/**
 * 判断是否不相交辅助类
 */
public final class BooleanDisjointHelper {

    public BooleanDisjointHelper() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 简单几何图形的不相交操作（点/线字符串/多边形）
     *
     * @param geometry1 图形1，只支持 Line、POINT、POLYGON
     * @param geometry2 图形2，只支持 Line、POINT、POLYGON
     * @return 如果不相交则返回true
     */
    public static boolean disjoint(Geometry geometry1, Geometry geometry2) {
        GeometryType t1 = geometry1.type(), t2 = geometry2.type();
        switch (t1) {
            case POINT:
                switch (t2) {
                    case POINT:
                        return !TaleHelper.equals(Point.point(geometry1), Point.point(geometry2));
                    case LINE:
                        return !TaleHelper.isPointOnLine(Point.point(geometry1), Line.line(geometry2));
                    case POLYGON:
                        return !TaleBooleans.booleanPointInPolygon(Point.point(geometry1), geometry2);
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            case LINE:
                switch (t2) {
                    case POINT:
                        return !TaleHelper.isPointOnLine(Point.point(geometry2), Line.line(geometry1));
                    case LINE:
                        return !isLineOnLine(Line.line(geometry1), Line.line(geometry2));
                    case POLYGON:
                        return !isLineInPoly(Line.line(geometry1), Polygon.polygon(geometry2));
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            case POLYGON:
                switch (t2) {
                    case POINT:
                        return !TaleBooleans.booleanPointInPolygon(Point.point(geometry1), Polygon.polygon(geometry1));
                    case LINE:
                        return !isLineInPoly(Line.line(geometry2), Polygon.polygon(geometry1));
                    case POLYGON:
                        return !isPolyInPoly(Polygon.polygon(geometry2), Polygon.polygon(geometry1));
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            default:
                throw new TaleException("geometry1 " + t1 + " not supported");
        }
    }

    private static boolean isLineOnLine(Line line1, Line line2) {
        List<Point> doLinesIntersect = TaleMisc.lineIntersect(line1, line2);
        return doLinesIntersect != null && !doLinesIntersect.isEmpty();
    }

    private static boolean isLineInPoly(Line line, Polygon polygon) {
        List<Point> linePoints = line.coordinates();
        for (Point pt : linePoints) {
            if (TaleBooleans.booleanPointInPolygon(pt, polygon)) {
                return true;
            }
        }

        List<Point> doLinesIntersect = TaleMisc.lineIntersect(line, TaleFeatureConversion.polygonToLine(polygon));
        return doLinesIntersect != null && !doLinesIntersect.isEmpty();
    }

    private static boolean isPolyInPoly(Polygon polygon1, Polygon polygon2) {
        for (Point pt : polygon1.coordinates()) {
            if (TaleBooleans.booleanPointInPolygon(pt, polygon2)) {
                return true;
            }
        }
        for (Point pt : polygon2.coordinates()) {
            if (TaleBooleans.booleanPointInPolygon(pt, polygon1)) {
                return true;
            }
        }

        List<Point> doLinesIntersect = TaleMisc.lineIntersect(TaleFeatureConversion.polygonToLine(polygon1),
                TaleFeatureConversion.polygonToLine(polygon2));
        return doLinesIntersect != null && !doLinesIntersect.isEmpty();
    }

}
