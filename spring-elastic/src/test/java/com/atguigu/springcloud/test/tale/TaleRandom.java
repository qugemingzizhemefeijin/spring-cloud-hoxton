package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.BoundingBox;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.util.TaleHelper;

import java.util.ArrayList;
import java.util.List;

public final class TaleRandom {

    private TaleRandom() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 随机点<br><br>
     *
     * @return 返回一个随机点
     */
    public static Point randomPoint() {
        return randomPoint(1, null).get(0);
    }

    /**
     * 随机点<br><br>
     * <p>
     * 返回指定数量的随机点
     *
     * @param num 将生成多少个点，最少为1个
     * @return 返回随机点集合
     */
    public static List<Point> randomPoint(int num) {
        return randomPoint(num, null);
    }

    /**
     * 随机点<br><br>
     * <p>
     * 返回指定数量的随机点
     *
     * @param num  将生成多少个点，最少为1个
     * @param bbox 指定随机点的生成边界
     * @return 返回随机点集合
     */
    public static List<Point> randomPoint(int num, BoundingBox bbox) {
        double[] bb = null;
        if (bbox != null) {
            TaleHelper.validateBBox((bb = bbox.bbox()));
        }
        if (num <= 0) {
            num = 1;
        }
        List<Point> pointList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            pointList.add(randomPositionUnchecked(bb));
        }
        return pointList;
    }

    private static Point randomPositionUnchecked(double[] bbox) {
        if (bbox != null) {
            return coordInBBox(bbox);
        }

        return Point.fromLngLat(lon(), lat());
    }

    private static Point coordInBBox(double[] bbox) {
        return Point.fromLngLat(
                Math.random() * (bbox[2] - bbox[0]) + bbox[0],
                Math.random() * (bbox[3] - bbox[1]) + bbox[1]
        );
    }

    private static double lon() {
        return rnd() * 360;
    }

    private static double lat() {
        return rnd() * 180;
    }

    private static double rnd() {
        return Math.random() - 0.5;
    }

}
