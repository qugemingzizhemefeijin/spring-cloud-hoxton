package com.atguigu.springcloud.test.tale.util;

import com.atguigu.springcloud.test.tale.TaleMeasurement;
import com.atguigu.springcloud.test.tale.TaleMeta;
import com.atguigu.springcloud.test.tale.enums.Orientation;
import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.BoundingBox;
import com.atguigu.springcloud.test.tale.shape.Geometry;
import com.atguigu.springcloud.test.tale.shape.GeometryType;
import com.atguigu.springcloud.test.tale.shape.Point;

public final class TransformScaleHelper {

    public TransformScaleHelper() {
        throw new AssertionError("No Instances.");
    }

    public static Point defineOrigin(Geometry geometry, Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.CENTROID;
        }

        BoundingBox bbox = TaleMeasurement.bbox(geometry);

        double west = bbox.west(), south = bbox.south(), east = bbox.east(), north = bbox.north();

        switch (orientation) {
            case SW:
            case SOUTH_WEST:
            case WEST_SOUTH:
            case BOTTOM_LEFT:
                return Point.fromLngLat(west, south);
            case SE:
            case SOUTH_EAST:
            case EAST_SOUTH:
            case BOTTOM_RIGHT:
                return Point.fromLngLat(east, south);
            case NW:
            case NORTH_WEST:
            case WEST_NORTH:
            case TOP_LEFT:
                return Point.fromLngLat(west, north);
            case NE:
            case NORTH_EAST:
            case EAST_NORTH:
            case TOP_RIGHT:
                return Point.fromLngLat(east, north);
            case CENTER:
                return TaleMeasurement.center(geometry);
            case CENTROID:
                return TaleMeasurement.centroid(geometry);
            default:
                throw new TaleException("invalid origin");
        }
    }

    /**
     * 缩放
     *
     * @param geometry 要缩放的组件
     * @param factor   缩放比
     * @param origin   缩放开始的点
     * @return 返回缩放后的组件
     */
    public static <T extends Geometry> T scale(T geometry, double factor, Point origin) {
        // 点不支持缩放，factory=1也不支持
        if (factor == 1 || geometry.type() == GeometryType.POINT) {
            return geometry;
        }

        // Scale each coordinate
        TaleMeta.coordEach(geometry, (g, coord, index, multiIndex, geomIndex) -> {
            double originalDistance = TaleMeasurement.rhumbDistance(origin, coord);
            double bearing = TaleMeasurement.rhumbBearing(origin, coord);
            double newDistance = originalDistance * factor;
            Point newCoord = TaleMeasurement.rhumbDestination(origin, newDistance, bearing);

            // 修改原坐标点
            coord.setLongitude(newCoord.getLongitude());
            coord.setLatitude(newCoord.getLatitude());

            return true;
        });

        return geometry;
    }

}
