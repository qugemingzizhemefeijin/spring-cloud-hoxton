package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

public final class TaleBooleans {

    private TaleBooleans() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 判断一条线是顺时针还是逆时针
     * @param line 线图形
     * @return true是顺时针,false逆时针
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

}
