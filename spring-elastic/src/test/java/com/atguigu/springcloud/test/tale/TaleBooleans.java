package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;
import com.atguigu.springcloud.test.tale.util.Equality;
import com.atguigu.springcloud.test.tale.util.TaleHelper;
import com.atguigu.springcloud.test.tale.util.TalePointInPolygonHelper;

import java.util.List;

public final class TaleBooleans {

    private TaleBooleans() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 判断一条线是顺时针还是逆时针
     *
     * @param line 线图形
     * @return true是顺时针, false逆时针
     */
    public static boolean booleanClockwise(Line line) {
        return booleanClockwise(line.coordinates());
    }

    /**
     * 判断坐标集合是顺时针还是逆时针
     *
     * @param points 坐标点集合
     * @return true是顺时针, false逆时针
     */
    public static boolean booleanClockwise(List<Point> points) {
        int sum = 0, i = 1, length = points.size();
        Point cur = points.get(0);
        Point prev;

        while (i < length) {
            prev = cur;
            cur = points.get(i);
            sum += (cur.getLongitude() - prev.getLongitude()) * (cur.getLatitude() + prev.getLatitude());
            i++;
        }
        return sum > 0;
    }

    /**
     * 确定相同类型的两个几何图形是否具有相同的X、Y坐标值。（注意：这里x,y坐标的小数默认为6位相同）
     *
     * @param g1 图形组件
     * @param g2 图形组件
     * @return 如果对象相等，则返回true,否则返回false
     */
    public static boolean booleanEqual(Geometry g1, Geometry g2) {
        return Equality.compare(g1, g2, 6);
    }

    /**
     * 确定相同类型的两个几何图形是否具有相同的X、Y坐标值。
     *
     * @param g1        图形组件
     * @param g2        图形组件
     * @param precision 坐标的小数精度（超出精度四舍五入）
     * @return 如果对象相等，则返回true,否则返回false
     */
    public static boolean booleanEqual(Geometry g1, Geometry g2, int precision) {
        return Equality.compare(g1, g2, precision);
    }

    /**
     * 如果line1的每个线段与line2的对应线段平行，则返回True。
     *
     * @param line1 线段1
     * @param line2 线段2
     * @return 当线段平行，则返回true
     */
    public static boolean booleanParallel(Line line1, Line line2) {
        if (line1 == null) {
            throw new TaleException("line1 is required");
        }
        if (line2 == null) {
            throw new TaleException("line2 is required");
        }

        List<Line> segments1 = TaleMisc.lineSegment(TaleCoordinateMutation.cleanCoords(line1));
        List<Line> segments2 = TaleMisc.lineSegment(TaleCoordinateMutation.cleanCoords(line2));

        for (int i = 0, size1 = segments1.size(), size2 = segments2.size(); i < size1; i++) {
            if (i >= size2) {
                break;
            }
            if (!isParallel(segments1.get(i), segments2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个线条的斜率并返回结果
     *
     * @param line1 线段1
     * @param line2 线段2
     * @return 如果斜率相等返回true
     */
    private static boolean isParallel(Line line1, Line line2) {
        double slope1 = TaleHelper.bearingToAzimuth(TaleMeasurement.rhumbBearing(line1.coordinates().get(0), line1.coordinates().get(1)));
        double slope2 = TaleHelper.bearingToAzimuth(TaleMeasurement.rhumbBearing(line2.coordinates().get(0), line2.coordinates().get(1)));

        return slope1 == slope2;
    }

    /**
     * 判断点是否在多边形内，如果点在多边形的边界上，也算在内。
     *
     * @param point          要判断的点
     * @param polygon        多边形
     * @return 如果点在多边形内，则返回true;否则返回false
     */
    public static boolean booleanPointInPolygon(Point point, Polygon polygon) {
        return booleanPointInPolygon(point, polygon, false);
    }

    /**
     * 判断点是否在多边形内
     *
     * @param point          要判断的点
     * @param polygon        多边形
     * @param ignoreBoundary 是否忽略多边形边界（true如果点在多边形的边界上不算，false则也算在多边形内）
     * @return 如果点在多边形内，则返回true;否则返回false
     */
    public static boolean booleanPointInPolygon(Point point, Polygon polygon, boolean ignoreBoundary) {
        if (point == null) {
            throw new TaleException("point is required");
        }
        if (polygon == null) {
            throw new TaleException("polygon is required");
        }

        BoundingBox bbox = TaleMeasurement.bbox(polygon);
        if (!TaleHelper.inBBox(point, bbox)) {
            return false;
        }

        int polyResult = TalePointInPolygonHelper.pointInPolygon(point, polygon);
        if (polyResult == 0) {
            return !ignoreBoundary;
        } else {
            return polyResult == 1;
        }
    }

    /**
     * 如果点位于直线上，则返回 true。默认不忽略忽略线段的起始和终止顶点.
     *
     * @param point             要判断的点
     * @param line              线
     * @return boolean
     */
    public static boolean booleanPointOnLine(Point point, Line line) {
        return booleanPointOnLine(point, line, false, null);
    }

    /**
     * 如果点位于直线上，则返回 true。接受可选参数以忽略线串的开始和结束顶点。
     *
     * @param point             要判断的点
     * @param line              线
     * @param ignoreEndVertices 是否忽略线段的起始和终止顶点
     * @param epsilon           要与交叉乘积结果进行比较的小数。用于处理浮点，例如经纬度点
     * @return boolean
     */
    public static boolean booleanPointOnLine(Point point, Line line, boolean ignoreEndVertices, Number epsilon) {
        if (point == null) {
            throw new TaleException("point is required");
        }
        if (line == null) {
            throw new TaleException("line is required");
        }

        List<Point> pointList = line.coordinates();
        if (pointList == null || pointList.isEmpty()) {
            return false;
        }

        for (int i = 0, size = pointList.size(); i < size - 1; i++) {
            int ignoreBoundary = 0;
            if (ignoreEndVertices) {
                if (i == 0) {
                    ignoreBoundary = 1; // start
                }
                if (i == size - 2) {
                    ignoreBoundary = 2; // end
                }
                if (i == 0 && i + 1 == size - 1) {
                    ignoreBoundary = 3; // both
                }
            }

            if (isPointOnLineSegment(pointList.get(i), pointList.get(i + 1), point, ignoreBoundary, epsilon)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断点是否在线的两端之间
     *
     * @param lineSegmentStart 线段开始坐标对的行首
     * @param lineSegmentEnd   线段结束坐标对的行尾
     * @param point            判断的点
     * @param excludeBoundary  排除边界是否允许点落在线端，0不忽略，1开头，2结尾，3中间
     * @param epsilon          要与交叉乘积结果进行比较的小数。用于处理浮点，例如经纬度点
     * @return boolean
     */
    private static boolean isPointOnLineSegment(Point lineSegmentStart, Point lineSegmentEnd, Point point, int excludeBoundary, Number epsilon) {
        double x = point.getX(), y = point.getY();
        double x1 = lineSegmentStart.getX(), y1 = lineSegmentStart.getY();
        double x2 = lineSegmentEnd.getX(), y2 = lineSegmentEnd.getY();
        double dxc = x - x1, dyc = y - y1;
        double dxl = x2 - x1, dyl = y2 - y1;
        double cross = dxc * dyl - dyc * dxl;

        if (epsilon != null) {
            if (Math.abs(cross) > epsilon.doubleValue()) {
                return false;
            }
        } else if (cross != 0) {
            return false;
        }

        boolean dxlGtdyl = Math.abs(dxl) >= Math.abs(dyl);
        if (excludeBoundary == 0) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 <= x && x <= x2 : x2 <= x && x <= x1;
            }
            return dyl > 0 ? y1 <= y && y <= y2 : y2 <= y && y <= y1;
        } else if (excludeBoundary == 1) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 < x && x <= x2 : x2 <= x && x < x1;
            }
            return dyl > 0 ? y1 < y && y <= y2 : y2 <= y && y < y1;
        } else if (excludeBoundary == 2) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 <= x && x < x2 : x2 < x && x <= x1;
            }
            return dyl > 0 ? y1 <= y && y < y2 : y2 < y && y <= y1;
        } else if (excludeBoundary == 3) {
            if (dxlGtdyl) {
                return dxl > 0 ? x1 < x && x < x2 : x2 < x && x < x1;
            }
            return dyl > 0 ? y1 < y && y < y2 : y2 < y && y < y1;
        }

        return false;
    }

}
