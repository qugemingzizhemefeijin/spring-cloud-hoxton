package com.atguigu.springcloud.test.geohash;

import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GeoHashUtils {

    private final static int GEO_HASH_LENGTH = 8;

    public static String encodeGeoHash(GeoLocation point) {
        return GeoHash.encodeHash(point.getLatitude(), point.getLongitude(), GEO_HASH_LENGTH);
    }

    public static String encodeGeoHash(GeoLocation point, int geoHashLen) {
        return GeoHash.encodeHash(point.getLatitude(), point.getLongitude(), geoHashLen);
    }

    public static List<String> getNearByGeoHash(String geohash) {
        return GeoHash.neighbours(geohash);
    }

    public static GeoLocation decodeGeoHash(String geohash) {
        LatLong decodeHash = GeoHash.decodeHash(geohash);
        return new GeoLocation(decodeHash.getLon(), decodeHash.getLat());
    }

    public static Coverage getRangeList(double topLeftLat, double topLeftLon, double bottomRightLat, double bottomRightLon) {
        Coverage coverage = GeoHash.coverBoundingBox(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon, GEO_HASH_LENGTH);
        log.debug("coverage:  [size:" + coverage.getHashes().size() + ", ratio:" + coverage.getRatio() + "]");

        return coverage;
    }

    public static void main(String[] args) {
        GeoLocation point = new GeoLocation(116.39507, 39.95145);
        System.out.println(encodeGeoHash(point));
        System.out.println(encodeGeoHash(point, 6));
        System.out.println(getNearByGeoHash(encodeGeoHash(point)));

        System.out.println(decodeGeoHash(encodeGeoHash(point)));

        System.out.println(getRangeList(39.949459D, 116.362012D, 39.91095D, 116.441351D));
    }

}
