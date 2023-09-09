package com.atguigu.springcloud.test.tale.util.d3geo.rotation;

import com.atguigu.springcloud.test.tale.util.d3geo.Algorithm;
import com.atguigu.springcloud.test.tale.util.d3geo.Compose;
import com.atguigu.springcloud.test.tale.util.d3geo.util.MathUtil;

public final class Rotation {

    public Rotation() {
        throw new AssertionError("No Instances.");
    }

    public static Algorithm rotateRadians(double deltaLambda, double deltaPhi, double deltaGamma) {
        deltaLambda = deltaLambda % MathUtil.TAU;
        if (deltaLambda != 0) {
            if (deltaPhi != 0 || deltaGamma != 0) {
                return new Compose(new RotationLambda(deltaLambda), new RotationPhiGamma(deltaPhi, deltaGamma));
            } else {
                return new RotationLambda(deltaLambda);
            }
        } else {
            if (deltaPhi != 0 || deltaGamma != 0) {
                return new RotationPhiGamma(deltaPhi, deltaGamma);
            } else {
                return new RotationIdentity();
            }
        }
    }

}
