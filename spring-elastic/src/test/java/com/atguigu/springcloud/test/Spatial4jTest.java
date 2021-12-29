package com.atguigu.springcloud.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.SpatialRelation;

@Slf4j
public class Spatial4jTest {

    private static ShapeFactory shapeFactory;

    @BeforeClass
    public static void init() {
        // spatial4j API http://locationtech.github.io/spatial4j/apidocs/
        // spatial4j github https://github.com/locationtech/spatial4j
        // https://cloud.tencent.com/developer/article/1902879
        // http://cn.voidcc.com/question/p-miwdeunz-ru.html

        // Spatial4j 的主要部分是它的形状集合。 Spatial4j 中的形状具有以下特征：
        // 计算其经纬度边界框。
        // 计算面积。 对于某些形状，它更多是估计值。
        // 计算它是否包含提供的点。
        // 计算与经纬度矩形的关系。 关系是：包含、内、不相交、相交。 请注意，Spatial4j 没有“触摸”的概念。

        // 形状   欧几里得	  圆柱形	       球形
        // 点	    Y	        Y	        Y
        // 长方形	Y	        Y	        Y
        // 圆	    Y	        N	        Y
        // 线	    Y	        N	        N
        // 缓冲 L/S	Y	        N	        N
        // 多边形	Y	        Y	        N
        // 形状集合	Y	        Y	        Y

        // 核心功能 :
        // 1.提供公共图形,可工作在Euclidean和geodesic(球体的表面)的模型
        // 2.提供距离计算和其它数学计算
        // 3.从WKT格式化字符串来读取形状

        // SpatialContext:
        // 如果您想要典型的大地测量环境，只需参考 GEO。(地理)
        // JtsSpatialContext
        // 通过使用 JTS 支持多边形（和其他几何图形）来增强默认的 SpatialContext。(几何)

        // 基于JTS的context
        JtsSpatialContextFactory jtsSpatialContextFactory = new JtsSpatialContextFactory();
        JtsSpatialContext jtsSpatialContext = jtsSpatialContextFactory.newSpatialContext();
        shapeFactory = jtsSpatialContext.getShapeFactory();
        //shapeFactory = SpatialContext.GEO.getShapeFactory();
    }

    /**
     * 判断传入的两个围栏是否相交
     * @param areaA 围栏A
     * @param areaB 围栏B
     * @return boolean
     */
    public static boolean intersects(double[][] areaA, double[][] areaB) {
        return intersects(createPolygon(areaA), createPolygon(areaB));
    }

    /**
     * 判断传入的两个围栏是否相交
     * @param polygonA 围栏A
     * @param areaB    围栏B
     * @return boolean
     */
    public static boolean intersects(Shape polygonA, double[][] areaB) {
        return intersects(polygonA, createPolygon(areaB));
    }

    /**
     * 判断传入的两个围栏是否相交
     * @param polygonA 围栏A
     * @param polygonB 围栏B
     * @return boolean
     */
    public static boolean intersects(Shape polygonA, Shape polygonB) {
        return polygonA.relate(polygonB) != SpatialRelation.DISJOINT;
    }

    /**
     * 根据围栏坐标创建一个多边形图形
     * @param area 围栏坐标
     * @return Shape
     */
    public static Shape createPolygon(double[][] area) {
        // 创建多边形
        ShapeFactory.PolygonBuilder polygonBuilder = shapeFactory.polygon();
        for(double[] p : area) {
            polygonBuilder.pointXY(p[0], p[1]);
        }
        return polygonBuilder.build();
    }

    /**
     * 判断传入的坐标点是否在围栏中
     * @param area 围栏坐标集合
     * @param lng  判断的点经度
     * @param lat  判断的点纬度
     * @return boolean
     */
    public static boolean contains(double[][] area, double lng, double lat) {
        return contains(createPolygon(area), shapeFactory.pointXY(lng, lat));
    }

    /**
     * 判断传入的坐标点是否在围栏中
     * @param polygon 多边形围栏
     * @param lng     判断的点经度
     * @param lat     判断的点纬度
     * @return boolean
     */
    public static boolean contains(Shape polygon, double lng, double lat) {
        return polygon.relate(shapeFactory.pointXY(lng, lat)) == SpatialRelation.CONTAINS;
    }

    /**
     * 判断传入的坐标点是否在围栏中
     * @param polygon 多边形围栏
     * @param point   经纬度坐标点
     * @return boolean
     */
    public static boolean contains(Shape polygon, Point point) {
        return polygon.relate(point) == SpatialRelation.CONTAINS;
    }

    /**
     * 判断传入的坐标点是否在指定的圆中
     * @param centerLng 圆中心经度
     * @param centerLat 圆中心纬度
     * @param distance  圆半径，单位米
     * @param lng       判断的点经度
     * @param lat       判断的点纬度
     * @return boolean
     */
    public static boolean contains(double centerLng, double centerLat, double distance, double lng, double lat) {
        return contains(createCircle(centerLng, centerLat, distance), shapeFactory.pointXY(lng, lat));
    }

    /**
     * 创建一个圆
     * @param lng      圆中心点经度
     * @param lat      圆中心点纬度
     * @param distance 单位米
     * @return Shape
     */
    public static Shape createCircle(double lng, double lat, double distance) {
        return shapeFactory.circle(lng, lat, DistanceUtils.dist2Degrees(distance / 1000, DistanceUtils.EARTH_MEAN_RADIUS_KM));
    }

    @Test
    public void testCircleContainsPoint() {
        boolean b = contains(116.33009D, 39.90728, 5000, 116.396939D, 39.921382);
        log.info("故宫点是否在圆中：{}", b);
        b = contains(116.33009D, 39.90728, 5000, 116.3812D, 39.918919);
        log.info("西单商场点是否在圆中：{}", b);
    }

    @Test
    public void testPolygonContainsPoint() {
        // 定义一个面
        Shape polygonA = createPolygon(new double[][]{new double[]{116.28044, 39.913773}, new double[]{116.279945, 39.917263}, new double[]{116.28196, 39.92645}, new double[]{116.28973, 39.92634}, new double[]{116.29138, 39.930504}, new double[]{116.3116, 39.929943}, new double[]{116.316605, 39.9299}, new double[]{116.33293, 39.929085}, new double[]{116.33617, 39.92888}, new double[]{116.34127, 39.9286}, new double[]{116.34135, 39.923607}, new double[]{116.34116, 39.919666}, new double[]{116.34393, 39.919666}, new double[]{116.3483, 39.91964}, new double[]{116.352425, 39.91964}, new double[]{116.35694, 39.919624}, new double[]{116.36066, 39.919624}, new double[]{116.36335, 39.919678}, new double[]{116.3633, 39.916954}, new double[]{116.36323, 39.91481}, new double[]{116.36324, 39.912777}, new double[]{116.36324, 39.90554}, new double[]{116.36328, 39.904793}, new double[]{116.36526, 39.904987}, new double[]{116.36738, 39.905178}, new double[]{116.36971, 39.905346}, new double[]{116.37201, 39.90551}, new double[]{116.37431, 39.905594}, new double[]{116.38103, 39.905926}, new double[]{116.38114, 39.905014}, new double[]{116.38103, 39.90374}, new double[]{116.381, 39.90294}, new double[]{116.38092, 39.902054}, new double[]{116.38103, 39.90061}, new double[]{116.38096, 39.89934}, new double[]{116.3811, 39.897846}, new double[]{116.38117, 39.895355}, new double[]{116.38081, 39.889263}, new double[]{116.35595, 39.8886}, new double[]{116.35559, 39.87398}, new double[]{116.34258, 39.87368}, new double[]{116.33806, 39.873734}, new double[]{116.33338, 39.873844}, new double[]{116.32807, 39.873817}, new double[]{116.30087, 39.872433}, new double[]{116.29033, 39.871975}, new double[]{116.288605, 39.87408}, new double[]{116.28788, 39.875874}, new double[]{116.28687, 39.877316}, new double[]{116.286446, 39.878532}, new double[]{116.28426, 39.88122}, new double[]{116.28386, 39.881996}, new double[]{116.28358, 39.882996}, new double[]{116.28329, 39.883713}, new double[]{116.28322, 39.884487}, new double[]{116.282715, 39.88554}, new double[]{116.28207, 39.887424}, new double[]{116.281136, 39.892017}, new double[]{116.280556, 39.907406}, new double[]{116.28044, 39.913773}});

        // 判断多边形与点的关系
        log.info("复兴门是否在围栏中: {}", contains(polygonA, 116.364528, 39.912832));
        log.info("广电国际酒店是否在围栏中: {}", contains(polygonA, 116.361716, 39.909449));
    }

}
