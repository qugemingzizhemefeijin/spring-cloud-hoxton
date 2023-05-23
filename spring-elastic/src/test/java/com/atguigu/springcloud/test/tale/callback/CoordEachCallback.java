package com.atguigu.springcloud.test.tale.callback;

import com.atguigu.springcloud.test.tale.shape.Point;

@FunctionalInterface
public interface CoordEachCallback {

    /**
     * 循环处理组件的Point信息
     * @param point      当前的点
     * @param index      循环位置
     * @param multiIndex 组合点循环位置，MULTI_LINE和MULTI_POLYGON的上层位置
     * @param geomIndex  GEOMETRY_COLLECTION的位置
     * @return 每个Point处理是否成功，当返回false时，循环即被中断
     */
    boolean accept(Point point, int index, int multiIndex, int geomIndex);

}
