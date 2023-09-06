package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleMeasurement;
import com.atguigu.springcloud.test.tale.TaleMeta;
import com.atguigu.springcloud.test.tale.enums.Units;
import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.GeometryCollection;
import com.atguigu.springcloud.test.tale.shape.GeometryType;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.ArrayList;
import java.util.List;

public final class BufferHelper {

    private BufferHelper() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 计算缓冲区单个组件
     *
     * @param geometry 要计算缓存区的组件
     * @param radius   绘制缓冲区的距离（允许负值）
     * @param units    距离单位
     * @param steps    频数
     * @return 计算后的组件
     */
    public static Geometry buffer(Geometry geometry, int radius, Units units, int steps) {
        if (geometry.type() == GeometryType.GEOMETRY_COLLECTION) {
            List<Geometry> results = new ArrayList<>();
            TaleMeta.geomEach(geometry, (g, parent, geomIndex) -> {
                Geometry buffered = buffer(g, radius, units, steps);
                if (buffered != null) {
                    results.add(buffered);
                }
                return true;
            });

            return results.isEmpty() ? null : GeometryCollection.fromGeometries(results);
        }

        // Project GeoJSON to Azimuthal Equidistant projection (convert to Meters)

        // JSTS buffer operation

        // Detect if empty geometries

        // Unproject coordinates (convert to Degrees)

        return null;
    }

    private static double defineProjection(Geometry geometry) {
        Point coords = TaleMeasurement.center(geometry);

    }

}
