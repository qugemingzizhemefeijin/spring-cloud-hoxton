package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.enums.Orientation;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;
import com.atguigu.springcloud.test.tale.util.BezierSpline;
import com.atguigu.springcloud.test.tale.util.TailClipHelper;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.util.TransformScaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TaleTransformation {

    private static final int DEFAULT_STEPS = 64;

    private TaleTransformation() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 给定一个中心点和要求的半径，使用默认的64步长绘制一个多边形的圆
     *
     * @param center 中心点位
     * @param radius 半径，单位公里
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius) {
        return circle(center, radius, DEFAULT_STEPS, Units.KILOMETERS);
    }

    /**
     * 给定一个中心点和要求的半径，使用默认的64步长绘制一个多边形的圆
     *
     * @param center 中心点位
     * @param radius 半径
     * @param units  半径单位
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius, Units units) {
        return circle(center, radius, DEFAULT_STEPS, units);
    }

    /**
     * 给定一个中心点和要求的半径，绘制一个多边形的圆
     *
     * @param center 中心点位
     * @param radius 半径
     * @param steps  步长
     * @param units  半径单位
     * @return 多边形圆
     */
    public static Polygon circle(Point center, double radius, int steps, Units units) {
        List<Point> coordinate = new ArrayList<>(steps + 1);
        for (int i = 0; i < steps; i++) {
            coordinate.add(TaleMeasurement.destination(center, radius, i * 360d / steps, units));
        }

        if (coordinate.size() > 0) {
            coordinate.add(Point.fromLngLat(coordinate.get(0)));
        }
        return Polygon.fromLngLats(coordinate);
    }

    /**
     * 深度克隆一个 geometry 对象
     *
     * @param geometry 图形组件
     * @return 返回克隆后的图形组件
     */
    @SuppressWarnings("unchecked")
    public static <T extends Geometry> T clone(T geometry) {
        if (geometry == null) {
            return null;
        }

        if (geometry instanceof CoordinateContainer) {
            return ((CoordinateContainer<?, T>) geometry).deepClone();
        } else if (geometry instanceof GeometryCollection) {
            return (T) ((GeometryCollection) geometry).deepClone();
        } else {
            throw new TaleException("geometry not support deepClone");
        }
    }

    /**
     * 边界裁剪<br>
     * 使用lineclip将该图形裁切到bbox。在裁剪多边形时可能导致退化边缘。<br>
     * 图形支持：POLYGON、MULTI_POLYGON、LINE、MULTI_LINE
     *
     * @param geometry 图形组件
     * @param bbox     裁剪区域
     * @return 返回裁剪后的图形，这里如果是LINE类型的话有可能会返回MultiLine，需要自己判断。
     */
    public static Geometry bboxClip(Geometry geometry, BoundingBox bbox) {
        switch (geometry.type()) {
            case LINE:
            case MULTI_LINE: {
                List<List<Point>> lines = new ArrayList<>();
                List<List<Point>> coords;
                if (geometry.type() == GeometryType.LINE) {
                    coords = new ArrayList<>(1);
                    coords.add(((Line) geometry).coordinates());
                } else {
                    coords = ((MultiLine) geometry).coordinates();
                }

                for (List<Point> line : coords) {
                    TailClipHelper.lineclip(line, bbox, lines);
                }

                if (lines.size() == 1) {
                    return Line.fromLngLats(lines.get(0));
                } else {
                    return MultiLine.fromLngLats(lines);
                }
            }
            case POLYGON:
                return Polygon.fromLngLats(TailClipHelper.clipPolygon(((Polygon) geometry).coordinates(), bbox));
            case MULTI_POLYGON: {
                List<List<Point>> coords = ((MultiPolygon) geometry).coordinates();
                return MultiPolygon.fromLngLats(coords.stream().map(poly -> TailClipHelper.clipPolygon(poly, bbox)).collect(Collectors.toList()));
            }
            default:
                throw new TaleException("geometry " + geometry.type() + " not supported");
        }
    }

    /**
     * 获取一条直线应用 贝塞尔算法 (opens new window)返回一个贝塞尔曲线。duration默认为 {@link BezierSpline#DURATION}，sharpness 默认为 {@link BezierSpline#SHARPNESS}
     * @param line 线条组件
     * @return 返回线条组件
     */
    public static Line bezierSpline(Line line) {
        return bezierSpline(line, BezierSpline.DURATION, BezierSpline.SHARPNESS);
    }

    /**
     * 获取一条直线应用 贝塞尔算法 (opens new window)返回一个贝塞尔曲线。sharpness 默认为 {@link BezierSpline#SHARPNESS}
     * @param line     线条组件
     * @param duration 相邻两个点之间的时间间隔（以毫秒为单位）
     * @return 返回线条组件
     */
    public static Line bezierSpline(Line line, int duration) {
        return bezierSpline(line, duration, BezierSpline.SHARPNESS);
    }

    /**
     * 获取一条直线应用 贝塞尔算法 (opens new window)返回一个贝塞尔曲线。
     * @param line      线条组件
     * @param duration  相邻两个点之间的时间间隔（以毫秒为单位）
     * @param sharpness 样条线之间路径的弯曲值
     * @return 返回线条组件
     */
    public static Line bezierSpline(Line line, int duration, double sharpness) {
        List<Point> coords = BezierSpline.bezierSpline(line.coordinates(), duration, sharpness);
        if (coords.isEmpty()) {
            return null;
        }
        return Line.fromLngLats(coords);
    }

    /**
     * 缩放
     * <p>
     * 将GeoJSON从质心缩放一个因子（例如：factor=2会使GeoJSON大200%）。
     *
     * @param geometry    要缩放的组件
     * @param factor      缩放比
     * @return 返回缩放后的图形组件
     */
    public static <T extends Geometry> T transformScale(T geometry, double factor) {
        Point origin = TransformScaleHelper.defineOrigin(geometry, null);

        return transformScale(geometry, factor, origin, false);
    }

    /**
     * 缩放
     * <p>
     * 将GeoJSON从给定点缩放一个因子（例如：factor=2会使GeoJSON大200%）。
     *
     * @param geometry    要缩放的组件
     * @param factor      缩放比
     * @param orientation 缩放开始的点位，默认为质心
     * @return 返回缩放后的图形组件
     */
    public static <T extends Geometry> T transformScale(T geometry, double factor, Orientation orientation) {
        Point origin = TransformScaleHelper.defineOrigin(geometry, orientation);

        return transformScale(geometry, factor, origin, false);
    }

    /**
     * 缩放
     * <p>
     * 将GeoJSON从给定点缩放一个因子（例如：factor=2会使GeoJSON大200%）。
     *
     * @param geometry    要缩放的组件
     * @param factor      缩放比
     * @param orientation 缩放开始的点位，默认为质心
     * @param mutate      是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回缩放后的图形组件
     */
    public static <T extends Geometry> T transformScale(T geometry, double factor, Orientation orientation, boolean mutate) {
        Point origin = TransformScaleHelper.defineOrigin(geometry, orientation);

        return transformScale(geometry, factor, origin, mutate);
    }

    /**
     * 缩放
     * <p>
     * 将GeoJSON从给定点缩放一个因子（例如：factor=2会使GeoJSON大200%）。
     *
     * @param geometry 要缩放的组件
     * @param factor   缩放比
     * @param origin   缩放开始的点
     * @return 返回缩放后的图形组件
     */
    public static <T extends Geometry> T transformScale(T geometry, double factor, Point origin) {
        return transformScale(geometry, factor, origin, false);
    }

    /**
     * 缩放
     * <p>
     * 将GeoJSON从给定点缩放一个因子（例如：factor=2会使GeoJSON大200%）。
     *
     * @param geometry 要缩放的组件
     * @param factor   缩放比
     * @param origin   缩放开始的点
     * @param mutate   是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回缩放后的图形组件
     */
    public static <T extends Geometry> T transformScale(T geometry, double factor, Point origin, boolean mutate) {
        T newGeometry = mutate ? geometry : TaleTransformation.clone(geometry);

        return TransformScaleHelper.scale(newGeometry, factor, origin);
    }

}
