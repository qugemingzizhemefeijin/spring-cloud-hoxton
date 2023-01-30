package com.atguigu.springcloud.other.hdrHistogram;

import org.HdrHistogram.Histogram;

public class HdrHistogramTest {

    public static void main(String[] args) {
        // 需指定预估的最大值
        Histogram histogram = new Histogram(5400000000000L, 4);
        for (int i = 1; i < 10000000; i = i * 2) {
            // 塞入需要计算的值
            histogram.recordValue(i);
        }
        long t1 = System.nanoTime();
        // 求出平均值
        double a = histogram.getMean();
        long t2 = System.nanoTime();
        System.out.println(a + " " + (t2 - t1) + "ns");

        histogram.outputPercentileDistribution(System.out, 1000.0);
    }

}
