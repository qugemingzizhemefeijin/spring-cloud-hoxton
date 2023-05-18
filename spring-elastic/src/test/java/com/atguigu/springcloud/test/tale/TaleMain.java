package com.atguigu.springcloud.test.tale;

import cn.hutool.json.JSONUtil;
import com.atguigu.springcloud.test.tale.shape.Line;
import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

public class TaleMain {

    public static void main(String[] args) {
        double[][] coordinates = new double[][] {new double[]{116.54849,39.94737},new double[]{116.57106,39.94676},new double[]{116.57602,39.946262},new double[]{116.59377,39.94571},new double[]{116.59241,39.953728},new double[]{116.59075,39.96269},new double[]{116.58996,39.97148},new double[]{116.580475,39.970818},new double[]{116.5776,39.982704},new double[]{116.55151,39.98088},new double[]{116.54735,39.986187},new double[]{116.53484,39.988842},new double[]{116.532486,39.987984},new double[]{116.53153,39.987846},new double[]{116.530655,39.987362},new double[]{116.53071,39.986824},new double[]{116.52999,39.986862},new double[]{116.52931,39.986824},new double[]{116.528465,39.986145},new double[]{116.525986,39.986794},new double[]{116.52528,39.986755},new double[]{116.524796,39.9867},new double[]{116.52327,39.98612},new double[]{116.52645,39.98193},new double[]{116.528534,39.979084},new double[]{116.52084,39.976414},new double[]{116.51824,39.975544},new double[]{116.515526,39.975323},new double[]{116.51567,39.9687},new double[]{116.51567,39.968716},new double[]{116.515656,39.963795},new double[]{116.51571,39.96179},new double[]{116.51562,39.953354},new double[]{116.51842,39.953922},new double[]{116.51794,39.95619},new double[]{116.52595,39.956146},new double[]{116.54088,39.956535},new double[]{116.5471,39.949787},new double[]{116.54805,39.947}, new double[]{116.54849,39.94737}};

        //Polygon polygon = Polygon.fromLngLats(coordinates);
        Line line = Line.fromLngLats(coordinates);

        List<Point> points = TaleMisc.kinks(line);

        System.out.println(JSONUtil.toJsonStr(points));
    }

}
