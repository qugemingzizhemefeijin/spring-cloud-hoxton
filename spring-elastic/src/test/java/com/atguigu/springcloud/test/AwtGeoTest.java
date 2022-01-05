package com.atguigu.springcloud.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

@Slf4j
public class AwtGeoTest {

    @Test
    public void testCircleContainsPoint() {
        Area polygon = createCircle(116.33009, 39.90728, 5000);

        log.info("故宫点是否在圆中：{}", contains(polygon, 116.396939, 39.921382));
        log.info("西单商场点是否在圆中：{}", contains(polygon, 116.3812, 39.918919));
    }

    @Test
    public void testPolygonContainsPoint() {
        Area polygon = createPolygon(new double[][]{new double[]{116.28044, 39.913773}, new double[]{116.279945, 39.917263}, new double[]{116.28196, 39.92645}, new double[]{116.28973, 39.92634}, new double[]{116.29138, 39.930504}, new double[]{116.3116, 39.929943}, new double[]{116.316605, 39.9299}, new double[]{116.33293, 39.929085}, new double[]{116.33617, 39.92888}, new double[]{116.34127, 39.9286}, new double[]{116.34135, 39.923607}, new double[]{116.34116, 39.919666}, new double[]{116.34393, 39.919666}, new double[]{116.3483, 39.91964}, new double[]{116.352425, 39.91964}, new double[]{116.35694, 39.919624}, new double[]{116.36066, 39.919624}, new double[]{116.36335, 39.919678}, new double[]{116.3633, 39.916954}, new double[]{116.36323, 39.91481}, new double[]{116.36324, 39.912777}, new double[]{116.36324, 39.90554}, new double[]{116.36328, 39.904793}, new double[]{116.36526, 39.904987}, new double[]{116.36738, 39.905178}, new double[]{116.36971, 39.905346}, new double[]{116.37201, 39.90551}, new double[]{116.37431, 39.905594}, new double[]{116.38103, 39.905926}, new double[]{116.38114, 39.905014}, new double[]{116.38103, 39.90374}, new double[]{116.381, 39.90294}, new double[]{116.38092, 39.902054}, new double[]{116.38103, 39.90061}, new double[]{116.38096, 39.89934}, new double[]{116.3811, 39.897846}, new double[]{116.38117, 39.895355}, new double[]{116.38081, 39.889263}, new double[]{116.35595, 39.8886}, new double[]{116.35559, 39.87398}, new double[]{116.34258, 39.87368}, new double[]{116.33806, 39.873734}, new double[]{116.33338, 39.873844}, new double[]{116.32807, 39.873817}, new double[]{116.30087, 39.872433}, new double[]{116.29033, 39.871975}, new double[]{116.288605, 39.87408}, new double[]{116.28788, 39.875874}, new double[]{116.28687, 39.877316}, new double[]{116.286446, 39.878532}, new double[]{116.28426, 39.88122}, new double[]{116.28386, 39.881996}, new double[]{116.28358, 39.882996}, new double[]{116.28329, 39.883713}, new double[]{116.28322, 39.884487}, new double[]{116.282715, 39.88554}, new double[]{116.28207, 39.887424}, new double[]{116.281136, 39.892017}, new double[]{116.280556, 39.907406}, new double[]{116.28044, 39.913773}});

        // 判断多边形与点的关系
        log.info("复兴门是否在围栏中: {}", contains(polygon, 116.364528, 39.912832));
        log.info("广电国际酒店是否在围栏中: {}", contains(polygon, 116.361716, 39.909449));
    }

    private static GeneralPath buildPath(double[][] area) {
        GeneralPath path = new GeneralPath();
        path.moveTo(area[0][0], area[0][1]);
        for (int i = 1; i < area.length; i++) {
            path.lineTo(area[i][0], area[i][1]);
        }
        // 这里判断一下，如果第一个点和最后一个点，不一样，则需要将第一个点添加到最后，形成闭合区间
        double[] lastArea = area[area.length - 1];
        if(area[0][0] != lastArea[0] || area[0][1] != lastArea[1]) {
            path.lineTo(area[0][0], area[0][1]);
        }
        path.closePath();
        return path;
    }

    private static Area createPolygon(double[][] area) {
        GeneralPath path = buildPath(area);
        path.closePath();
        return new Area(path);
    }

    /**
     * 判断传入的坐标点是否在围栏中
     * @param area 围栏坐标集合
     * @param lng  判断的点经度
     * @param lat  判断的点纬度
     * @return boolean
     */
    public static boolean contains(double[][] area, double lng, double lat) {
        return contains(createPolygon(area), lng, lat);
    }

    /**
     * 判断传入的坐标点是否在围栏中
     * @param polygon 多边形围栏
     * @param lng     判断的点经度
     * @param lat     判断的点纬度
     * @return boolean
     */
    public static boolean contains(Area polygon, double lng, double lat) {
        return polygon.contains(lng, lat);
    }

    /**
     * 创建一个圆
     * @param lng     圆心经度
     * @param lat     圆心纬度
     * @param radius  半径，米
     * @return Area
     */
    public static Area createCircle(double lng, double lat, int radius) {
        double degree = (24901 * 1609) / 360.0;
        double radiusLat = 1 / degree * radius;
        double radiusLng = 1 / (degree * Math.cos(lat * (Math.PI / 180))) * radius;

        Ellipse2D ellipse = new Ellipse2D.Double(lng - radiusLng, lat - radiusLat, radiusLng * 2, radiusLat * 2);
        return new Area(ellipse);
    }

}
