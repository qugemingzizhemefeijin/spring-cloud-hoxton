package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.exception.TaleException;
import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.MultiPolygon;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public final class TaleFeatureConversion {

    public TaleFeatureConversion() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 多边形转换成多线<br><br>
     * <p>
     * 将多边形转换为(多)线段或将多面体转换为包含(多)线段
     *
     * @param polygon 多边形
     * @return Line
     */
    public static Line polygonToLine(Polygon polygon) {
        if (polygon == null) {
            throw new TaleException("polygon is required");
        }
        return Line.fromLngLats(polygon.coordinates());
    }

    /**
     * 多边形转换成多线<br><br>
     * <p>
     * 将多边形转换为(多)线段或将多面体转换为包含(多)线段
     *
     * @param multiPolygon 自核多边形
     * @return List<Line>
     */
    public static List<Line> polygonToLine(MultiPolygon multiPolygon) {
        if (multiPolygon == null) {
            throw new TaleException("polygon is required");
        }

        List<List<Point>> allPoint = multiPolygon.coordinates();
        List<Line> lines = new ArrayList<>(allPoint.size());
        for (List<Point> ps : allPoint) {
            lines.add(Line.fromLngLats(ps));
        }

        return lines;
    }

}
