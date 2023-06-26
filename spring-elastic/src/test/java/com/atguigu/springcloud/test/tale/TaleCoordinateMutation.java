package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.CoordinateContainer;
import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.GeometryType;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.util.TaleHelper;

import java.util.*;

public final class TaleCoordinateMutation {

    private TaleCoordinateMutation() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 获取输入feature并将它们的所有坐标从[x, y]翻转为[y, x]，默认会生成新的 geometry 图形组件
     *
     * @param geometry 图形组件
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T flip(T geometry) {
        return flip(geometry, false);
    }

    /**
     * 获取输入feature并将它们的所有坐标从[x, y]翻转为[y, x]
     *
     * @param geometry 图形组件
     * @param mutate   是否影响原图形坐标
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T flip(T geometry, boolean mutate) {
        T newGeometry;

        // 确保我们不会直接修改传入的geometry对象。
        if (!mutate) {
            newGeometry = TaleTransformation.clone(geometry);
        } else {
            newGeometry = geometry;
        }

        TaleMeta.coordEach(geometry, (geo, p, index, multiIndex, geomIndex) -> {
            double latitude = p.getLatitude(), longitude = p.getLongitude();

            // 翻转
            p.setLongitude(latitude);
            p.setLatitude(longitude);

            return true;
        });

        return newGeometry;
    }

    /**
     * 坐标小数处理，默认情况保留6位小数
     *
     * @param geometry 图形组件
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T truncate(T geometry) {
        return truncate(geometry, 6, false);
    }

    /**
     * 坐标小数处理
     *
     * @param geometry  图形组件
     * @param precision 保留的小数位数，如果传入空则保留6位小数
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T truncate(T geometry, Integer precision) {
        return truncate(geometry, 6, false);
    }

    /**
     * 坐标小数处理
     *
     * @param geometry  图形组件
     * @param precision 保留的小数位数，如果传入空则保留6位小数
     * @param mutate    是否影响原图形坐标
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T truncate(T geometry, Integer precision, boolean mutate) {
        if (precision == null || precision < 0) {
            precision = 6;
        }

        T newGeometry;
        // 确保我们不会直接修改传入的geometry对象。
        if (!mutate) {
            newGeometry = TaleTransformation.clone(geometry);
        } else {
            newGeometry = geometry;
        }

        double factor = Math.pow(10, precision);

        TaleMeta.coordEach(geometry, (geo, p, index, multiIndex, geomIndex) -> {
            double latitude = p.getLatitude(), longitude = p.getLongitude();

            // 翻转
            p.setLongitude(Math.round(latitude * factor) / factor);
            p.setLatitude(Math.round(longitude * factor) / factor);

            return true;
        });

        return newGeometry;
    }

    /**
     * 清除重复坐标点
     *
     * @param geometry 图形组件
     * @param mutate   是否影响原图形坐标
     * @return 返回处理后的图形组件
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Geometry> T cleanCoords(T geometry, boolean mutate) {
        if (geometry.type() == GeometryType.POINT) {
            return geometry;
        }

        T newGeometry;
        // 确保我们不会直接修改传入的geometry对象。
        if (!mutate) {
            newGeometry = TaleTransformation.clone(geometry);
        } else {
            newGeometry = geometry;
        }

        TaleMeta.coordsEach(newGeometry, (geo, pointList, multiIndex, geomIndex) -> {
            if (geo.type() == GeometryType.POINT) {
                return true;
            }

            List<Point> newPointList;
            if (geo.type() == GeometryType.MULTI_POINT) {
                newPointList = cleanPoint(pointList);
            } else {
                newPointList = cleanLine(pointList);
            }

            ((CoordinateContainer)geo).setCoordinates(newPointList);

            return true;
        });

        return newGeometry;
    }

    /**
     * 处理点性质的重复点集合
     * @param points 点集合
     * @return 处理完的新的集合
     */
    private static List<Point> cleanPoint(List<Point> points) {
        int size = points.size();
        if (size == 2 && !TaleHelper.equals(points.get(0), points.get(1))) {
            return points;
        }

        Set<String> existing = new HashSet<>();
        List<Point> newPoints = new ArrayList<>();
        for (Point point : points) {
            String key = point.getLongitude()+"-"+point.getLatitude();
            if (existing.add(key)) {
                newPoints.add(point);
            }
        }

        return newPoints;
    }

    /**
     * 处理带有线条性质的重复点集合
     * @param points 点集合
     * @return 处理完的新的集合
     */
    private static List<Point> cleanLine(List<Point> points) {
        int size = points.size();
        if (size == 2 && !TaleHelper.equals(points.get(0), points.get(1))) {
            return points;
        }

        List<Point> newPoints = new ArrayList<>();
        int secondToLast = points.size() - 1;
        int newPointsLength = newPoints.size();

        newPoints.add(points.get(0));
        for (int i = 1; i < secondToLast; i++) {
            Point prevAddedPoint = newPoints.get(newPoints.size() - 1);
            Point currPoint = points.get(i);

            // 如果当前点与前一个点相同，则直接返回
            if (currPoint.getLongitude() == prevAddedPoint.getLatitude()
                    && currPoint.getLatitude() == prevAddedPoint.getLatitude()) {
                continue;
            }

            newPoints.add(currPoint);
            newPointsLength = newPoints.size();
            if (newPointsLength > 2) {
                // 如果最后第二个元素在最后第三个元素与最后一个元素的线上，则直接将其删除掉
                if (isPointOnLineSegment(newPoints.get(newPointsLength - 3),
                        newPoints.get(newPointsLength - 1),
                        newPoints.get(newPointsLength - 2))) {
                    newPoints.remove(newPointsLength - 2);
                }
            }
        }

        Point lastPoint = points.get(points.size() - 1);

        newPoints.add(lastPoint);
        newPointsLength = newPoints.size();

        // 如果第一个点与最后一个点相同，但是点却小于4个，则是一个错误的Polygon图形
        if (TaleHelper.equals(points.get(0), lastPoint) && newPointsLength < 4) {
            throw new TaleException("invalid polygon");
        }

        // 如果最后第二个元素在最后第三个元素与最后一个元素的线上，则直接将其删除掉
        if (isPointOnLineSegment(newPoints.get(newPointsLength - 3),
                newPoints.get(newPointsLength - 1),
                newPoints.get(newPointsLength - 2))) {
            newPoints.remove(newPointsLength - 2);
        }

        return newPoints;
    }

    /**
     * 判断point是否位于start和end之间的线段上
     *
     * @param start 开始的点
     * @param end   结束的点
     * @param point 需要判断的点
     * @return 在线段上则返回true
     */
    private static boolean isPointOnLineSegment(Point start, Point end, Point point) {
        double x = point.getLongitude(), y = point.getLatitude();
        double startX = start.getLongitude(), startY = start.getLatitude();
        double endX = end.getLongitude(), endY = end.getLatitude();

        double dxc = x - startX;
        double dyc = y - startY;
        double dxl = endX - startX;
        double dyl = endY - startY;
        double cross = dxc * dyl - dyc * dxl;

        if (cross != 0) {
            return false;
        } else if (Math.abs(dxl) >= Math.abs(dyl)) {
            return dxl > 0 ? startX <= x && x <= endX : endX <= x && x <= startX;
        } else {
            return dyl > 0 ? startY <= y && y <= endY : endY <= y && y <= startY;
        }
    }

    /**
     * Rewind Line、MultiLine、Polygon、MultiPolygon、GeometryCollection 默认按照顺时针返回
     *
     * @param geometry 图形组件
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T rewind(T geometry) {
        return rewind(geometry, true, false);
    }

    /**
     * Rewind Line、MultiLine、Polygon、MultiPolygon、GeometryCollection 默认按照顺时针返回
     *
     * @param geometry 图形组件
     * @param mutate   是否影响原图形坐标
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T rewind(T geometry, boolean mutate) {
        return rewind(geometry, true, mutate);
    }

    /**
     * Rewind Line、MultiLine、Polygon、MultiPolygon、GeometryCollection true顺时针和false逆时针
     *
     * @param geometry 图形组件
     * @param reverse  true顺时针和false逆时针
     * @param mutate   是否影响原图形坐标
     * @return 返回处理后的图形组件
     */
    public static <T extends Geometry> T rewind(T geometry, boolean reverse, boolean mutate) {
        T newGeometry;

        // 确保我们不会直接修改传入的geometry对象。
        if (!mutate) {
            newGeometry = TaleTransformation.clone(geometry);
        } else {
            newGeometry = geometry;
        }

        if (newGeometry.type() == GeometryType.GEOMETRY_COLLECTION) {
            TaleMeta.geomEach(newGeometry, (geo, parentGeo, geomIndex) -> {
                rewindGeo(geo, reverse);
                return true;
            });

            return newGeometry;
        } else {
            return rewindGeo(newGeometry, reverse);
        }
    }

    /**
     * Rewind Line、MultiLine、Polygon、MultiPolygon、GeometryCollection true顺时针和false逆时针
     * @param geometry 图形组件
     * @param reverse  true顺时针和false逆时针
     * @return 返回处理后的图形组件
     */
    private static <T extends Geometry> T rewindGeo(T geometry, boolean reverse) {
        GeometryType type = geometry.type();

        // Support all GeoJSON Geometry Objects
        switch (type) {
            case GEOMETRY_COLLECTION:
                TaleMeta.geomEach(geometry, (geo, parentGeo, geomIndex) -> {
                    rewindGeo(geo, reverse);
                    return true;
                });
                return geometry;
            case LINE:
            case POLYGON:
            case MULTI_LINE:
            case MULTI_POLYGON:
                TaleMeta.coordsEach(geometry, (geo, pointList, multiIndex, geomIndex) -> {
                    rewindCoords(pointList, reverse);
                    return true;
                });
                return geometry;
            case POINT:
            case MULTI_POINT:
                return geometry;
        }
        return null;
    }

    /**
     * Rewind - true顺时针和false逆时针
     * @param coords  点集合
     * @param reverse true顺时针和false逆时针
     */
    private static void rewindCoords(List<Point> coords, boolean reverse) {
        if (TaleBooleans.booleanClockwise(coords) != reverse) {
            Collections.reverse(coords);
        }
    }

}
