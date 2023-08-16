package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleBooleans;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;

/**
 * 判断是否包含辅助类
 */
public class BooleanContainsHelper {

    public BooleanContainsHelper() {
        throw new AssertionError("No Instances.");
    }

    public static boolean booleanContains(Geometry geometry1, Geometry geometry2) {
        GeometryType t1 = geometry1.type(), t2 = geometry2.type();
        switch (t1) {
            case POINT:
                if (t2 == GeometryType.POINT) {
                    return TaleHelper.equals(Point.point(geometry1), Point.point(geometry2));
                }
                throw new TaleException("geometry2 " + t2 + " not supported");
            case MULTI_POINT:
                switch (t2) {
                    case POINT:
                        return TaleHelper.isPointInMultiPoint(Point.point(geometry2), MultiPoint.multiPoint(geometry1));
                    case MULTI_POINT:
                        return TaleHelper.isMultiPointInMultiPoint(MultiPoint.multiPoint(geometry2), MultiPoint.multiPoint(geometry1));
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            case LINE:
                switch (t2) {
                    case POINT:
                        return TaleHelper.isPointOnLine(Point.point(geometry2), Line.line(geometry1));
                    case LINE:
                        return TaleHelper.isLineOnLine(Line.line(geometry2), Line.line(geometry1));
                    case MULTI_POINT:
                        return TaleHelper.isMultiPointOnLine(MultiPoint.multiPoint(geometry2), Line.line(geometry1));
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            case POLYGON:
                switch (t2) {
                    case POINT:
                        return TaleBooleans.booleanPointInPolygon(Point.point(geometry2), Polygon.polygon(geometry1), true);
                    case LINE:
                        return TaleHelper.isLineInPolygon(Line.line(geometry2), Polygon.polygon(geometry2));
                    case POLYGON:
                        return TaleHelper.isPolygonInPolygon(Polygon.polygon(geometry2), Polygon.polygon(geometry1));
                    case MULTI_POINT:
                        return TaleHelper.isMultiPointInPolygon(MultiPoint.multiPoint(geometry2), Polygon.polygon(geometry1));
                    default:
                        throw new TaleException("geometry2 " + t2 + " not supported");
                }
            default:
                throw new TaleException("geometry1 " + t1 + " not supported");
        }
    }

}
