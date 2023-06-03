package com.atguigu.springcloud.test.tale.callback;

import com.atguigu.springcloud.test.tale.shape.Point;

import java.util.List;

@FunctionalInterface
public interface CoordsEachCallback {

    /**
     * 循环处理组件的Points信息
     * @param pointList  当前的集合点
     * @param multiIndex 组合点循环位置，MULTI_LINE和MULTI_POLYGON的上层位置
     * @param geomIndex  GEOMETRY_COLLECTION的位置
     * @return 每个Point处理是否成功，当返回false时，循环即被中断
     */
    boolean accept(List<Point> pointList, int multiIndex, int geomIndex);

}
