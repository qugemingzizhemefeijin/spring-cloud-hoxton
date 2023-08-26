package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleMeasurement;
import com.atguigu.springcloud.test.tale.TaleMeta;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.Point;

public final class TransformTranslateHelper {

    public TransformTranslateHelper() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 组件平移
     *
     * @param geometry  要平移的组件
     * @param distance  运动的长度;负值决定相反方向的运动
     * @param direction 向北的角度，顺时针正
     * @param units     距离单位，支持 MILES、 KILOMETERS、 DEGREES OR RADIANS
     * @return 返回组件对象
     */
    public static <T extends Geometry> T transformTranslate(T geometry, double distance, double direction, Units units) {
        // Invert with negative distances
        double d = distance < 0 ? -distance : distance;
        double bearing = distance < 0 ? direction + 180 : direction;

        TaleMeta.coordEach(geometry, (g, p, index, multiIndex, geomIndex) -> {
            Point newCoords = TaleMeasurement.rhumbDestination(p, d, bearing, units);

            p.setLongitude(newCoords.getLongitude());
            p.setLatitude(newCoords.getLatitude());

            return true;
        });

        return geometry;
    }

}
