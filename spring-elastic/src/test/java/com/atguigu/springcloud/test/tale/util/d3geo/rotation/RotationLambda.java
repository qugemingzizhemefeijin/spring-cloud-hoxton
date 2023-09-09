package com.atguigu.springcloud.test.tale.util.d3geo.rotation;

import com.atguigu.springcloud.test.tale.util.d3geo.Algorithm;
import com.atguigu.springcloud.test.tale.util.d3geo.util.MathUtil;

public class RotationLambda implements Algorithm {

    private final double deltaLambda;

    public RotationLambda(double deltaLambda) {
        this.deltaLambda = deltaLambda;
    }

    private double[] calc(double lambda, double phi, double deltaLambda) {
        lambda += deltaLambda;

        return new double[]{lambda > MathUtil.PI ? lambda - MathUtil.TAU : lambda < -MathUtil.PI ? lambda + MathUtil.TAU : lambda, phi};
    }

    @Override
    public double[] def(double lambda, double phi) {
        return calc(lambda, phi, deltaLambda);
    }

    @Override
    public double[] invert(double lambda, double phi) {
        return calc(lambda, phi, -deltaLambda);
    }

}
