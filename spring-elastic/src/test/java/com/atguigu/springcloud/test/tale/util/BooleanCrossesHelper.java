package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleBooleans;
import com.atguigu.springcloud.test.tale.TaleFeatureConversion;
import com.atguigu.springcloud.test.tale.TaleMisc;
import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.MultiPoint;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.shape.Polygon;

import java.util.List;

/**
 * 判断是否交叉的辅助类
 */
public final class BooleanCrossesHelper {

    public BooleanCrossesHelper() {
        throw new AssertionError("No Instances.");
    }

    public static boolean doMultiPointAndLineStringCross(MultiPoint multiPoint, Line line) {
        boolean foundIntPoint = false, foundExtPoint = false;
        List<Point> multiPointList = multiPoint.coordinates();
        List<Point> linePointList = line.coordinates();
        int pointLength = multiPointList.size(), lineLength = linePointList.size();
        int i = 0;

        while (i < pointLength && !foundIntPoint && !foundExtPoint) {
            for (int i2 = 0; i2 < lineLength - 1; i2++) {
                boolean incEndVertices = true;
                if (i2 == 0 || i2 == lineLength - 2) {
                    incEndVertices = false;
                }
                if (TaleHelper.isPointOnLineSegment(linePointList.get(i2), linePointList.get(i2 + 1), multiPointList.get(i), incEndVertices)) {
                    foundIntPoint = true;
                } else {
                    foundExtPoint = true;
                }
            }
            i++;
        }

        return foundIntPoint && foundExtPoint;
    }

    public static boolean doesMultiPointCrossPoly(MultiPoint multiPoint, Polygon polygon) {
        boolean foundIntPoint = false, foundExtPoint = false;
        List<Point> points = multiPoint.coordinates();
        for (int i = 0, pointLength = points.size(); i < pointLength && (!foundIntPoint || !foundExtPoint); i++) {
            if (TaleBooleans.booleanPointInPolygon(points.get(i), polygon)) {
                foundIntPoint = true;
            } else {
                foundExtPoint = true;
            }
        }

        return foundExtPoint && foundIntPoint;
    }

    public static boolean doLineStringsCross(Line line1, Line line2) {
        // 获取交叉点
        List<Point> doLinesIntersect = TaleMisc.lineIntersect(line1, line2);
        if (doLinesIntersect == null || doLinesIntersect.isEmpty()) {
            return false;
        }
        List<Point> linePointList1 = line1.coordinates(), linePointList2 = line2.coordinates();
        int size1 = linePointList1.size(), size2 = linePointList2.size();

        for (int i = 0; i < size1 - 1; i++) {
            for (int i2 = 0; i2 < size2 - 1; i2++) {
                boolean incEndVertices = true;
                if (i2 == 0 || i2 == size2 - 2) {
                    incEndVertices = false;
                }
                if (TaleHelper.isPointOnLineSegment(linePointList1.get(i), linePointList1.get(i + 1), linePointList2.get(i2), incEndVertices)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean doLineStringAndPolygonCross(Line line, Polygon polygon) {
        Line polygonToLine = TaleFeatureConversion.polygonToLine(polygon);
        List<Point> doLinesIntersect = TaleMisc.lineIntersect(line, polygonToLine);

        return doLinesIntersect != null && !doLinesIntersect.isEmpty();
    }

}
