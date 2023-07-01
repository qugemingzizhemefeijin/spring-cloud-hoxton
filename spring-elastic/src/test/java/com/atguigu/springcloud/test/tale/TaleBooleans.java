package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.util.Equality;

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

}
