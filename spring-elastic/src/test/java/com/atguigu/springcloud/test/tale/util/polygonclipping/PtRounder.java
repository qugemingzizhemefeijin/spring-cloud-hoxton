package com.atguigu.springcloud.test.tale.util.polygonclipping;

public class PtRounder {

    private CoordRounder xRounder;
    private CoordRounder yRounder;

    public PtRounder() {
        this.reset();
    }

    public void reset() {
        this.xRounder = new CoordRounder();
        this.yRounder = new CoordRounder();
    }

    public Location round(double x, double y) {
        return new Location(this.xRounder.round(x), this.yRounder.round(y));
    }

    public CoordRounder getxRounder() {
        return xRounder;
    }

    public CoordRounder getyRounder() {
        return yRounder;
    }
}
