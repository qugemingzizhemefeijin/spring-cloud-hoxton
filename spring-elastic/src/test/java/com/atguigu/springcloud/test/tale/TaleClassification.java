package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.MultiPoint;
import com.atguigu.springcloud.test.tale.shape.Point;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public final class TaleClassification {

    private TaleClassification() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 根据目标点计算出在点集合中距离此目标点最近的点
     *
     * @param targetPoint 目标点
     * @param points      点集合
     * @return 如果点集合为空则返回目标点，否则返回距其最近的点
     */
    public static Point nearestPoint(Point targetPoint, List<Point> points) {
        if (CollectionUtils.isEmpty(points)) {
            return targetPoint;
        }
        Point nearestPoint = points.get(0);
        double minDist = Double.POSITIVE_INFINITY;
        for (Point point : points) {
            double distanceToPoint = TaleMeasurement.distance(targetPoint, point);
            if (distanceToPoint < minDist) {
                nearestPoint = point;
                minDist = distanceToPoint;
            }
        }
        return nearestPoint;
    }

    /**
     * 根据目标点计算出在点集合中距离此目标点最近的点
     *
     * @param targetPoint 目标点
     * @param multiPoint  点集合
     * @return 如果点集合为空则返回目标点，否则返回距其最近的点
     */
    public static Point nearestPoint(Point targetPoint, MultiPoint multiPoint) {
        return nearestPoint(targetPoint, multiPoint.coordinates());
    }

}
