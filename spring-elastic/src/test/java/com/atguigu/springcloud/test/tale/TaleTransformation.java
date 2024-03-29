package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.enums.Orientation;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.*;
import com.atguigu.springcloud.test.tale.util.*;
import com.atguigu.springcloud.test.tale.util.polygonclipping.PolygonClipping;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
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
     *
     * @param line 线条组件
     * @return 返回线条组件
     */
    public static Line bezierSpline(Line line) {
        return bezierSpline(line, BezierSpline.DURATION, BezierSpline.SHARPNESS);
    }

    /**
     * 获取一条直线应用 贝塞尔算法 (opens new window)返回一个贝塞尔曲线。sharpness 默认为 {@link BezierSpline#SHARPNESS}
     *
     * @param line     线条组件
     * @param duration 相邻两个点之间的时间间隔（以毫秒为单位）
     * @return 返回线条组件
     */
    public static Line bezierSpline(Line line, int duration) {
        return bezierSpline(line, duration, BezierSpline.SHARPNESS);
    }

    /**
     * 获取一条直线应用 贝塞尔算法 (opens new window)返回一个贝塞尔曲线。
     *
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
     * @param geometry 要缩放的组件
     * @param factor   缩放比
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

    /**
     * 平移
     * <p>
     * 在给定的方向角上沿恒向线移动指定距离（单位公里）。
     *
     * @param geometry  要平移的组件
     * @param distance  运动的长度;负值决定相反方向的运动
     * @param direction 向北的角度，顺时针正
     * @param mutate    是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回平移后的图形组件
     */
    public static <T extends Geometry> T transformTranslate(T geometry, double distance, double direction, boolean mutate) {
        return transformTranslate(geometry, distance, direction, null, mutate);
    }

    /**
     * 平移
     * <p>
     * 在给定的方向角上沿恒向线移动指定距离（单位公里）。
     *
     * @param geometry  要平移的组件
     * @param distance  运动的长度;负值决定相反方向的运动
     * @param direction 向北的角度，顺时针正
     * @return 返回平移后的图形组件
     */
    public static <T extends Geometry> T transformTranslate(T geometry, double distance, double direction) {
        return transformTranslate(geometry, distance, direction, null, false);
    }

    /**
     * 平移
     * <p>
     * 在给定的方向角上沿恒向线移动指定距离。
     *
     * @param geometry  要平移的组件
     * @param distance  运动的长度;负值决定相反方向的运动
     * @param direction 向北的角度，顺时针正
     * @param units     距离单位，为空默认为公里，支持 MILES、 KILOMETERS、 DEGREES OR RADIANS
     * @return 返回平移后的图形组件
     */
    public static <T extends Geometry> T transformTranslate(T geometry, double distance, double direction, Units units) {
        return transformTranslate(geometry, distance, direction, units, false);
    }

    /**
     * 平移
     * <p>
     * 在给定的方向角上沿恒向线移动指定距离。
     *
     * @param geometry  要平移的组件
     * @param distance  运动的长度;负值决定相反方向的运动
     * @param direction 向北的角度，顺时针正
     * @param units     距离单位，为空默认为公里，支持 MILES、 KILOMETERS、 DEGREES OR RADIANS
     * @param mutate    是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回平移后的图形组件
     */
    public static <T extends Geometry> T transformTranslate(T geometry, double distance, double direction, Units units, boolean mutate) {
        // 没有产生任何位移，则直接返回
        if (distance == 0) {
            return geometry;
        }

        T newGeometry = mutate ? geometry : TaleTransformation.clone(geometry);

        double d = distance < 0 ? -distance : distance;
        double bearing = distance < 0 ? direction + 180 : direction;

        TaleMeta.coordEach(newGeometry, (g, p, index, multiIndex, geomIndex) -> {
            p.setCoordinates(TaleMeasurement.rhumbDestination(p, d, bearing, units));

            return true;
        });

        return newGeometry;
    }

    /**
     * 旋转（围绕质心）
     * <p>
     * 旋转任何组件，围绕其质心或一个给定的枢轴点;所有的旋转都遵循右手规则:https://en.wikipedia.org/wiki/righthand_rule
     *
     * @param geometry 要旋转的组件
     * @param angle    旋转角度，顺时针方向
     * @return 返回旋转后的图形组件
     */
    public static <T extends Geometry> T transformRotate(T geometry, double angle) {
        return transformRotate(geometry, angle, null, false);
    }

    /**
     * 旋转
     * <p>
     * 旋转任何组件，围绕其质心或一个给定的枢轴点;所有的旋转都遵循右手规则:https://en.wikipedia.org/wiki/righthand_rule
     *
     * @param geometry 要旋转的组件
     * @param angle    旋转角度，顺时针方向
     * @param pivot    围绕其执行旋转的点，为空默认为质心
     * @return 返回旋转后的图形组件
     */
    public static <T extends Geometry> T transformRotate(T geometry, double angle, Point pivot) {
        return transformRotate(geometry, angle, pivot, false);
    }

    /**
     * 旋转
     * <p>
     * 旋转任何组件，围绕其质心或一个给定的枢轴点;所有的旋转都遵循右手规则:https://en.wikipedia.org/wiki/righthand_rule
     *
     * @param geometry 要旋转的组件
     * @param angle    旋转角度，顺时针方向
     * @param pivot    围绕其执行旋转的点，为空默认为质心
     * @param mutate   是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回旋转后的图形组件
     */
    public static <T extends Geometry> T transformRotate(T geometry, double angle, Point pivot, boolean mutate) {
        if (angle == 0) {
            return geometry;
        }

        Point rotatePoint = pivot == null ? TaleMeasurement.centroid(geometry) : pivot;
        T newGeometry = mutate ? geometry : TaleTransformation.clone(geometry);

        // Rotate each coordinate
        TaleMeta.coordEach(newGeometry, (g, p, index, multiIndex, geomIndex) -> {
            double initialAngle = TaleMeasurement.rhumbBearing(rotatePoint, p);
            double finalAngle = initialAngle + angle;
            double distance = TaleMeasurement.rhumbDistance(pivot, p);

            // 计算新的点位
            Point newCoords = TaleMeasurement.rhumbDestination(pivot, distance, finalAngle);
            // 更新点坐标
            p.setCoordinates(newCoords);

            return true;
        });

        return newGeometry;
    }

    /**
     * 多线段偏移
     * <p>
     * 获取一条线并返回具有指定距离偏移量的线。
     *
     * @param geometry 偏移的线段，仅支持 Line、MultiLine
     * @param distance 偏移线的距离（可以是负值），默认单位公里
     * @return 返回偏移后的线
     */
    public static <T extends Geometry> T lineOffset(T geometry, double distance) {
        return lineOffset(geometry, distance, null);
    }

    /**
     * 多线段偏移
     * <p>
     * 获取一条线并返回具有指定距离偏移量的线。
     *
     * @param geometry 偏移的线段，仅支持 Line、MultiLine
     * @param distance 偏移线的距离（可以是负值）
     * @param units    距离单位，支持 DEGREES，RADIANS，MILES，KILOMETERS，INCHES，YARDS，METERS，不填写则默认为 KILOMETERS
     * @return 返回偏移后的线
     */
    public static <T extends Geometry> T lineOffset(T geometry, double distance, Units units) {
        if (geometry == null) {
            throw new TaleException("geometry is required");
        }

        if (distance == 0) {
            return geometry;
        }
        if (units == null) {
            units = Units.KILOMETERS;
        }

        return LineOffsetHelper.lineOffset(geometry, distance, units);
    }

    /**
     * 多边形划分三角形<br>
     * 使用 earcut 算法将 Polygon、MultiPolygon 细分为多个三角形。
     *
     * @param geometry 仅支持Polygon、MultiPolygon
     * @return 三角形Polygon集合
     */
    public static List<Polygon> tesselate(Geometry geometry) {
        if (geometry == null) {
            throw new TaleException("geometry is required");
        }

        return TesselateHelper.tesselate(geometry);
    }

    /**
     * 简化多边形<br>
     * 获取对象并返回简化版本。内部使用 simplify-js 使用 Ramer–Douglas–Peucker 算法执行简化。
     *
     * @param geometry 要处理的图形组件
     * @return 返回简化后的图形
     */
    public static <T extends Geometry> T simplify(T geometry) {
        return simplify(geometry, 1, false, false);
    }

    /**
     * 简化多边形<br>
     * 获取对象并返回简化版本。内部使用 simplify-js 使用 Ramer–Douglas–Peucker 算法执行简化。
     *
     * @param geometry  要处理的图形组件
     * @param tolerance 简化公差，不能为负数
     * @return 返回简化后的图形
     */
    public static <T extends Geometry> T simplify(T geometry, double tolerance) {
        return simplify(geometry, tolerance, false, false);
    }

    /**
     * 简化多边形<br>
     * 获取对象并返回简化版本。内部使用 simplify-js 使用 Ramer–Douglas–Peucker 算法执行简化。
     *
     * @param geometry    要处理的图形组件
     * @param tolerance   简化公差，不能为负数
     * @param highQuality 是否花更多时间使用不同的算法创建更高质量的简化
     * @return 返回简化后的图形
     */
    public static <T extends Geometry> T simplify(T geometry, double tolerance, boolean highQuality) {
        return simplify(geometry, tolerance, highQuality, false);
    }

    /**
     * 简化多边形<br>
     * 获取对象并返回简化版本。内部使用 simplify-js 使用 Ramer–Douglas–Peucker 算法执行简化。
     *
     * @param geometry    要处理的图形组件
     * @param tolerance   简化公差，不能为负数
     * @param highQuality 是否花更多时间使用不同的算法创建更高质量的简化
     * @param mutate      是否影响原图形坐标（如果为真，性能将显著提高）
     * @return 返回简化后的图形
     */
    public static <T extends Geometry> T simplify(T geometry, double tolerance, boolean highQuality, boolean mutate) {
        if (geometry == null) {
            throw new TaleException("geometry is required");
        }
        if (tolerance < 0) {
            throw new TaleException("invalid tolerance");
        }
        T newGeometry = mutate ? geometry : TaleTransformation.clone(geometry);

        return SimplifyHelper.simplify(newGeometry, tolerance, highQuality);
    }

    /**
     * 联合<br>
     * 获取两个或多个多边形，并返回一个组合多边形。如果输入的多边形不是连续的，这个函数将返回一个MultiPolygon。
     *
     * @param geometry1 仅支持 Polygon和MultiPolygon
     * @param geometry2 仅支持 Polygon和MultiPolygon
     * @return 返回组合后的图形，如果输入为空，则为 null
     */
    public static Geometry union(Geometry geometry1, Geometry geometry2) {
        return polygonClipping(geometry1, geometry2, PolygonClipping::union);
    }

    /**
     * 计算交集<br>
     * 取两个多边形并找到它们的交点。如果它们共享一个边界，返回边界;如果它们不相交，返回
     *
     * @param geometry1 仅支持 Polygon和MultiPolygon
     * @param geometry2 仅支持 Polygon和MultiPolygon
     * @return 返回交集图形，如果输入为空，则为 null。如果它们不共享任何区域，则返回null
     */
    public static Geometry intersect(Geometry geometry1, Geometry geometry2) {
        return polygonClipping(geometry1, geometry2, PolygonClipping::intersection);
    }

    /**
     * 计算差异<br>
     * 通过裁剪第二个多边形来找到两个多边形之间的差异。
     *
     * @param geometry1 仅支持 Polygon和MultiPolygon
     * @param geometry2 仅支持 Polygon和MultiPolygon
     * @return 返回差异图形，如果输入为空，则为 null。如果它们有交集，则去除交集，保留差异部分。
     */
    public static Geometry difference(Geometry geometry1, Geometry geometry2) {
        return polygonClipping(geometry1, geometry2, PolygonClipping::difference);
    }

    /**
     * 计算补集<br>
     * 通过计算获取第二个图形相对第一个图形的补集。
     *
     * @param geometry1 仅支持 Polygon和MultiPolygon
     * @param geometry2 仅支持 Polygon和MultiPolygon
     * @return 返回补集图形，如果输入为空，则为 null。
     */
    public static Geometry xor(Geometry geometry1, Geometry geometry2) {
        return polygonClipping(geometry1, geometry2, PolygonClipping::xor);
    }

    /**
     * 调用 PolygonClipping 工具类，计算对应的多边形交集、差集、并集以及补集。
     *
     * @param geometry1        仅支持 Polygon和MultiPolygon
     * @param geometry2        仅支持 Polygon和MultiPolygon
     * @param clippingFunction 执行 PolygonClipping 的工具方法
     * @return 返回 Polygon、MultiPolygon、null
     */
    private static Geometry polygonClipping(Geometry geometry1, Geometry geometry2, BiFunction<List<List<Point>>, List<List<Point>>, List<List<Point>>> clippingFunction) {
        if (geometry1 == null && geometry2 == null) {
            return null;
        } else if (geometry1 == null) {
            return geometry2;
        } else if (geometry2 == null) {
            return geometry1;
        }

        GeometryType t1 = geometry1.type(), t2 = geometry2.type();
        if (t1 != GeometryType.POLYGON && t1 != GeometryType.MULTI_POLYGON) {
            throw new TaleException("geometry1 " + t1 + " not supported");
        }
        if (t2 != GeometryType.POLYGON && t2 != GeometryType.MULTI_POLYGON) {
            throw new TaleException("geometry2 " + t2 + " not supported");
        }

        // 转换成集合，交给 PolygonClipping 工具
        List<List<Point>> points1 = t1 == GeometryType.POLYGON ? Polygon.polygon(geometry1).multiCoordinates() : MultiPolygon.multiPolygon(geometry1).coordinates();
        List<List<Point>> points2 = t2 == GeometryType.POLYGON ? Polygon.polygon(geometry2).multiCoordinates() : MultiPolygon.multiPolygon(geometry2).coordinates();

        List<List<Point>> retPoints = clippingFunction.apply(points1, points2);

        if (retPoints == null || retPoints.isEmpty()) {
            return null;
        } else if (retPoints.size() == 1) {
            return Polygon.fromLngLats(retPoints.get(0));
        } else {
            return MultiPolygon.fromLngLats(retPoints);
        }
    }

    /**
     * 计算缓冲区（辐射区），距离单位默认为KILOMETERS，steps默认为8。<br>
     * 计算组件在给定半径的缓冲区。支持的单位有英里、公里和度数。<br>
     * 使用负半径时，生成的几何图形可能无效，如果与半径大小相比，它太小了。如果是集合类组件，输出集合的成员可能少于输入，甚至为空。
     *
     * @param geometry 要计算的组件
     * @param radius   绘制缓冲区的距离（允许负值）
     * @return 缓冲区的图形组件
     */
    public static Geometry buffer(Geometry geometry, int radius) {
        return buffer(geometry, radius, null, null);
    }

    /**
     * 计算缓冲区（辐射区），steps默认为8。<br>
     * 计算组件在给定半径的缓冲区。支持的单位有英里、公里和度数。<br>
     * 使用负半径时，生成的几何图形可能无效，如果与半径大小相比，它太小了。如果是集合类组件，输出集合的成员可能少于输入，甚至为空。
     *
     * @param geometry 要计算的组件
     * @param radius   绘制缓冲区的距离（允许负值）
     * @param units    距离单位，默认 KILOMETERS
     * @return 缓冲区的图形组件
     */
    public static Geometry buffer(Geometry geometry, int radius, Units units) {
        return buffer(geometry, radius, units, null);
    }

    /**
     * 计算缓冲区（辐射区）<br>
     * 计算组件在给定半径的缓冲区。支持的单位有英里、公里和度数。<br>
     * 使用负半径时，生成的几何图形可能无效，如果与半径大小相比，它太小了。如果是集合类组件，输出集合的成员可能少于输入，甚至为空。
     *
     * @param geometry 要计算的组件
     * @param radius   绘制缓冲区的距离（允许负值）
     * @param units    距离单位，默认 KILOMETERS
     * @param steps    频数，默认为 8
     * @return 缓冲区的图形组件
     */
    public static Geometry buffer(Geometry geometry, int radius, Units units, Integer steps) {
        if (geometry == null) {
            throw new TaleException("geometry is required");
        }
        if (units == null) {
            units = Units.KILOMETERS;
        }
        if (steps == null) {
            steps = 8;
        }

        GeometryType type = geometry.type();
        if (type == GeometryType.GEOMETRY_COLLECTION) {
            List<Geometry> geometryList = new ArrayList<>();
            Units finalUnits = units;
            Integer finalSteps = steps;

            TaleMeta.geomEach(geometry, (g, parent, geomIndex) -> {
                Geometry buffered = BufferHelper.buffer(g, radius, finalUnits, finalSteps);
                if (buffered != null) {
                    geometryList.add(buffered);
                }
                return true;
            });

            return geometryList.isEmpty() ? null : GeometryCollection.fromGeometries(geometryList);
        } else {
            return BufferHelper.buffer(geometry, radius, units, steps);
        }
    }

}
