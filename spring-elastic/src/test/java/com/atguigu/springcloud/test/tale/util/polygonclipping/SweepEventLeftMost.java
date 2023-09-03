package com.atguigu.springcloud.test.tale.util.polygonclipping;

public class SweepEventLeftMost {

    final double sine;
    final double cosine;

    public SweepEventLeftMost(double sine, double cosine) {
        this.sine = sine;
        this.cosine = cosine;
    }

    public static SweepEventLeftMost create(double sine, double cosine) {
        return new SweepEventLeftMost(sine, cosine);
    }

}
