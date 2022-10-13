package com.atguigu.springcloud.test.geohash;

public class GeoLocation {

    /**
     * 经度
     */
    private final double longitude;

    /**
     * 纬度
     */
    private final double latitude;

    /**
     * 构造函数
     * @param longitude - 经度
     * @param latitude - 纬度
     */
    public GeoLocation(double longitude , double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
            throw new IllegalArgumentException("The supplied coordinates " + this + " are out of range.");
        }
    }

    /**
     * 构造函数
     * @param location - Geo地理信息
     */
    public GeoLocation(GeoLocation location) {
        this(location.longitude , location.latitude);
    }

    /**
     * 获得纬度
     * @return double
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 获得经度
     * @return double
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "(" + longitude + "," + latitude + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GeoLocation) {
            GeoLocation other = (GeoLocation) obj;
            return latitude == other.latitude && longitude == other.longitude;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 42;
        long latBits = Double.doubleToLongBits(latitude);
        long lonBits = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (latBits ^ (latBits >>> 32));
        result = 31 * result + (int) (lonBits ^ (lonBits >>> 32));
        return result;
    }


}
