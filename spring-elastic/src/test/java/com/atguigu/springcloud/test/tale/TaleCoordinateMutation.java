package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.GeometryType;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.util.TaleHelper;

import java.util.ArrayList;
import java.util.List;

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

        TaleMeta.coordEach(geometry, (p, index, multiIndex, geomIndex) -> {
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

        TaleMeta.coordEach(geometry, (p, index, multiIndex, geomIndex) -> {
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
    public static <T extends Geometry> T cleanCoords(T geometry, boolean mutate) {
        if (geometry.type() == GeometryType.POINT) {
            return geometry;
        }

        TaleMeta.coordsEach(geometry, (pointList, multiIndex, geomIndex) -> {
            if (geometry.type() == GeometryType.MULTI_POINT) {

            } else {
                cleanLine(pointList);
            }
            return true;
        });

        return null;
    }

    private static List<Point> cleanLine(List<Point> points) {
        int size = points.size();
        if (size == 2 && !TaleHelper.equals(points.get(0), points.get(1))) {
            return points;
        }

        List<Point> newPoints = new ArrayList<>();
        int secondToLast = points.size() - 1;
        int newPointsLength = newPoints.size();

        // TODO

        return null;
    }

}
