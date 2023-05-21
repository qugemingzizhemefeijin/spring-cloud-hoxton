package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.shape.BoundingBox;
import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.Point;
import com.atguigu.springcloud.test.tale.util.Units;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaleMain {

    public static void main(String[] args) {
        // x, n, e, n
        // 0, 1, 2, 3
        // [0, 1], [2, 1], [e, n], [x, n]

        Line line = Line.fromLngLats(new double[][]{new double[]{109.502991, 29.68718}, new double[]{108.837829, 32.969237}, new double[]{113.567871, 37.200787}});
        Point point = TaleMeasurement.along(line, 300, Units.KILOMETERS);
        log.info("目标点：{}", point);

//        BoundingBox bbox = BoundingBox.fromLngLats(-20, -20, -15, 0);
//        BoundingBox newBbox = TaleMeasurement.square(bbox);
//        log.info("正方形：{}", newBbox);


//        Line line = Line.fromLngLats(new double[][]{new double[]{104.99467, 30.071677}, new double[]{107.13797, 36.550462}, new double[]{112.607082, 34.991467}});
//        BoundingBox bbox = TaleMeasurement.bbox(line);
//        System.out.println(bbox.west());
//        System.out.println(bbox.south());
//        System.out.println(bbox.east());
//        System.out.println(bbox.north());
//        Polygon bboxPolygon = TaleMeasurement.bboxPolygon(bbox);
//        log.info("多边形：{}", bboxPolygon.coordinates());

//        Polygon polygon = Polygon.fromLngLats(new double[][]{new double[]{-97.522259, 35.4691},new double[]{-97.502754, 35.463455}, new double[]{-97.508269, 35.463245}});
//        Point center = TaleMeasurement.center(polygon);
//        log.info("多边中心：{}", center);
//        Polygon polygon = Polygon.fromLngLats(new double[][]{new double[]{-81, 41}, new double[]{-88, 36}, new double[]{-84, 31}, new double[]{-80, 33}, new double[]{-77, 39}, new double[]{-81, 41}});
//        Point point = TaleMeasurement.centroid(polygon);
//
//        log.info("多边形质心：{}", point);

//        Line line = Line.fromLngLats(new double[][]{new double[]{115, -32}, new double[]{131, -22}, new double[]{143, -25}, new double[]{150, -34}});
//        double length = TaleMeasurement.length(line, Units.KILOMETERS);
//        log.info("多线段总周长：{}公里", length);

//        Point p1 = Point.fromLngLat(-75.343, 39.984);
//        Point p2 = Point.fromLngLat(-75.534, 39.123);
//        double rhumbBearing = TaleMeasurement.rhumbBearing(p1, p2);
//        log.info("计算两点恒向线夹角：{}", rhumbBearing);

//        Point p = Point.fromLngLat(-75.343, 39.984);
//        double distance = 50;
//        double bearing = 90;
//
//        Point destination = TaleMeasurement.destination(p, distance, bearing, Units.MILES);
//        log.info("计算的指定点：{}", destination);

//        Point p1 = Point.fromLngLat(144.834823, -37.771257);
//        Point p2 = Point.fromLngLat(145.14244, -37.830937);
//        Point midPoint = TaleMeasurement.midpoint(p1, p2);
//        log.info("计算两点中间点：{}", midPoint);

//        double[][] coordinates = new double[][] {new double[]{116.54849,39.94737},new double[]{116.57106,39.94676},new double[]{116.57602,39.946262},new double[]{116.59377,39.94571},new double[]{116.59241,39.953728},new double[]{116.59075,39.96269},new double[]{116.58996,39.97148},new double[]{116.580475,39.970818},new double[]{116.5776,39.982704},new double[]{116.55151,39.98088},new double[]{116.54735,39.986187},new double[]{116.53484,39.988842},new double[]{116.532486,39.987984},new double[]{116.53153,39.987846},new double[]{116.530655,39.987362},new double[]{116.53071,39.986824},new double[]{116.52999,39.986862},new double[]{116.52931,39.986824},new double[]{116.528465,39.986145},new double[]{116.525986,39.986794},new double[]{116.52528,39.986755},new double[]{116.524796,39.9867},new double[]{116.52327,39.98612},new double[]{116.52645,39.98193},new double[]{116.528534,39.979084},new double[]{116.52084,39.976414},new double[]{116.51824,39.975544},new double[]{116.515526,39.975323},new double[]{116.51567,39.9687},new double[]{116.51567,39.968716},new double[]{116.515656,39.963795},new double[]{116.51571,39.96179},new double[]{116.51562,39.953354},new double[]{116.51842,39.953922},new double[]{116.51794,39.95619},new double[]{116.52595,39.956146},new double[]{116.54088,39.956535},new double[]{116.5471,39.949787},new double[]{116.54805,39.947}, new double[]{116.54849,39.94737}};
//        Line line = Line.fromLngLats(coordinates);
//        List<Point> points = TaleMisc.kinks(line);
//        System.out.println(JSONUtil.toJsonStr(points));

//        Point ggPoint = Point.fromLngLat(116.396939, 39.921382);
//        Point xdscPoint = Point.fromLngLat(116.3812, 39.918919);
//
//        log.info("两点距离：{} 公里", TaleMeasurement.distance(ggPoint, xdscPoint, Units.KILOMETERS));

//        double[][] coordinates = new double[][]{new double[]{108.09876, 37.200787}, new double[]{106.398901, 33.648651}, new double[]{114.972103, 33.340483}, new double[]{113.715685, 37.845557}, new double[]{108.09876, 37.200787}};
//        Polygon polygon = Polygon.fromLngLats(coordinates);
//
//        log.info("面积：{} 平方公里", TaleMeasurement.area(polygon, Units.KILOMETERS));

//        Point p1 = Point.fromLngLat(109.104262, 37.831315);
//        Point p2 = Point.fromLngLat(102.865569, 34.089941);
//
//        double bearing = TaleMeasurement.bearing(p1, p2);
//        log.info("计算两点之间的角度：{}", bearing);
    }

}
