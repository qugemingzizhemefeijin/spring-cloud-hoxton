package com.atguigu.springcloud.test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.atguigu.springcloud.test.domain.CommunityInfo;
import com.atguigu.springcloud.test.domain.Pixel;
import com.atguigu.springcloud.test.domain.Point;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

// 百度地图获取小区边界代码
@Slf4j
public class BaiduMapCommunityTest {

    private static final String AK = "0000023ABCDEFE"; // 此处替换为自己申请的AK

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final double[] LG = new double[]{1.289059486E7, 8362377.87, 5591021, 3481989.83, 1678043.12, 0};
    private static final double[][] FP = new double[][]{
            new double[]{1.410526172116255E-8, 8.98305509648872E-6, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 1.73379812E7},
            new double[]{-7.435856389565537E-9, 8.983055097726239E-6, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 1.026014486E7},
            new double[]{-3.030883460898826E-8, 8.98305509983578E-6, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37},
            new double[]{-1.981981304930552E-8, 8.983055099779535E-6, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06},
            new double[]{3.09191371068437E-9, 8.983055096812155E-6, 6.995724062E-5, 23.10934304144901, -2.3663490511E-4, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4},
            new double[]{2.890871144776878E-9, 8.983055095805407E-6, -3.068298E-8, 7.47137025468032, -3.53937994E-6, -0.02145144861037, -1.234426596E-5, 1.0322952773E-4, -3.23890364E-6, 826088.5}
    };

    private static final int[] AU = new int[]{75, 60, 45, 30, 15, 0};
    private static final double[][] IG = new double[][] {
            {-0.0015702102444, 111320.7020616939, 1704480524535203D, -10338987376042340D, 26112667856603880D, -35149669176653700D, 26595700718403920D, -10725012454188240D, 1800819912950474D, 82.5},
            {8.277824516172526E-4, 111320.7020463578, 6.477955746671607E8, -4.082003173641316E9, 1.077490566351142E10, -1.517187553151559E10, 1.205306533862167E10, -5.124939663577472E9, 9.133119359512032E8, 67.5},
            {0.00337398766765, 111320.7020202162, 4481351.045890365, -2.339375119931662E7, 7.968221547186455E7, -1.159649932797253E8, 9.723671115602145E7, -4.366194633752821E7, 8477230.501135234, 52.5},
            {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5},
            {-3.441963504368392E-4, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5},
            {-3.218135878613132E-4, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}
    };

    // objectMapper.writeValueAsString(obj)
    public static List<CommunityInfo> getCommunityInfoList(String name) throws UnsupportedEncodingException {
        String url = "http://map.baidu.com/su?wd="+ URLEncoder.encode(name, "UTF-8") +"&cid=347&type=0&pc_ver=2";

        HttpRequest request = HttpUtil.createGet(url);
        request.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0");

        HttpResponse response = request.execute();
        log.info("status code : {}", response.getStatus());

        if (response.getStatus() != HttpStatus.HTTP_OK) {
            return null;
        }

        JSONArray json = JSONUtil.parseObj(response.body()).getJSONArray("s");

        List<CommunityInfo> list = Lists.newArrayListWithCapacity(json.size());
        for (Object o : json) {
            String[] s = ((String)o).split("\\$");

            CommunityInfo info = new CommunityInfo();
            info.setCityName(s[0]);
            info.setDistrict(s[1]);
            info.setCommunityName(s[3]);
            info.setUid(s[5]);

            list.add(info);
        }

        return list;
    }

    public static String getCommunityBorder(String uid) {
        String url = "http://map.baidu.com/?pcevaname=pc4.1&qt=ext&ext_ver=new&l=12&uid=" + uid;

        HttpRequest request = HttpUtil.createGet(url);
        request.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0");

        HttpResponse response = request.execute();
        log.info("status code : {}", response.getStatus());

        if (response.getStatus() != HttpStatus.HTTP_OK) {
            return null;
        }

        return JSONUtil.parseObj(response.body()).getJSONObject("content").getStr("geo");
    }

    // 此功能可以有替换算法（meter2DegreeLocal）
    private static Point meter2Degree(String x, String y) {
        String url = "http://api.map.baidu.com/geoconv/v1/?coords="+ x + "," + y + "&from=6&to=5&output=json&ak=" + AK;

        HttpRequest request = HttpUtil.createGet(url);
        request.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0");

        HttpResponse response = request.execute();
        log.info("status code : {}", response.getStatus());

        if (response.getStatus() != HttpStatus.HTTP_OK) {
            throw new RuntimeException("status code = " + response.getStatus());
        }

        JSONObject j = JSONUtil.parseObj(response.body()).getJSONArray("result").getJSONObject(0);
        return new Point(j.getDouble("x"), j.getDouble("y"));
    }

    private static <T> T calc(double x, double y, double[] b, CoordinateInterface<T> createCoordinate) {
        if (b == null) {
            return null;
        }

        double c = b[0] + b[1] * Math.abs(x);
        double d = Math.abs(y) / b[9];
        d = b[2] + b[3] * d + b[4] * d * d + b[5] * d * d * d + b[6] * d * d * d * d + b[7] * d * d * d * d * d + b[8] * d * d * d * d * d * d;
        c = c * (0 > x ? -1 : 1);
        d = d * (0 > y ? -1 : 1);

        return createCoordinate.getCoordinate(c, d);
    }

    private static Point meter2DegreeLocal(String x, String y) {
        double lng = Math.abs(Double.parseDouble(x)), lat = Math.abs(Double.parseDouble(y));
        double[] c = null;
        for (int d = 0; d < LG.length; d++) {
            if (lat >= LG[d]) {
                c = FP[d];
                break;
            }
        }

        return calc(lng, lat, c, Point::new);
    }

    private static List<Point> coordinateToPoints(String coordinates) {
        List<Point> pointList = Lists.newArrayList();

        if (coordinates.length() == 0 || !coordinates.contains("-")) {
            return null;
        }

        String[] v = coordinates.split("-");
        String temp_coordinates = v[1];
        if (temp_coordinates.length() == 0 || !temp_coordinates.contains(",")) {
            return null;
        }
        // 去掉最后的分号
        if (temp_coordinates.charAt(temp_coordinates.length() -1) == ';') {
            temp_coordinates = temp_coordinates.substring(0, temp_coordinates.length() - 1);
        }

        String[] points = temp_coordinates.split(",");
        for (int i = 0; i < points.length; i = i + 2) {
            String x = points[i], y = points[i + 1];

            //log.info("x={},y={}", x, y);
            // pointList.add(meter2Degree(x, y));
            pointList.add(meter2DegreeLocal(x, y));
        }
        return pointList;
    }

    private static double calcLng(double a, double b, double c) {
        while (a > c) {
            a -= c - b;
        }
        while (a < b) {
            a += c - b;
        }
        return a;
    }

    private static double calcLat(double a, double b, double c) {
        a = Math.max(a, b);
        a = Math.min(a, c);
        return a;
    }

    private static Pixel coordinateToPixels(double lng, double lat) {
        if (180 < lng || -180 > lng || 90 < lat || -90 > lat) {
            return new Pixel(0, 0);
        }

        double[] c = null;
        double x = calcLng(lng, -180, 180), y = calcLat(lat, -74, 74);
        for (int d = 0; d < AU.length; d++) {
            if (lat >= AU[d]) {
                c = IG[d];
                break;
            }
        }
        if (c != null) for (int d = 0; d < AU.length; d++) {
            if (lat <= -AU[d]) {
                c = IG[d];
                break;
            }
        }

        return calc(x, y, c, Pixel::new);
    }

    private static Pixel coordinateToPixels(Point point) {
        return coordinateToPixels(point.getLng(), point.getLat());
    }

    @FunctionalInterface
    private interface CoordinateInterface<T> {

        T getCoordinate(double x, double y);

    }

    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException {
        List<CommunityInfo> communityInfoList = getCommunityInfoList("南通兴东机场");
        log.info("{}", objectMapper.writeValueAsString(communityInfoList));
        if (CollectionUtil.isEmpty(communityInfoList)) {
            log.warn("communityInfoList isEmpty");
            return;
        }
        String coordinates = getCommunityBorder(communityInfoList.get(0).getUid());
        log.info("{}", coordinates);

        // 计算出经纬度坐标点
        log.info("{}", objectMapper.writeValueAsString(coordinateToPoints(coordinates)));

        log.info("{}", objectMapper.writeValueAsString(coordinateToPixels(119.23559623413306D, 34.60902146363141D)));

//        String body = "{\"p\":false,\"q\":\"金鹰国际城\",\"s\":[\"连云港市$海州区$$金鹰国际花园$347$bbb467bd96bc0a9ccac3db61$\",\"连云港市$海州区$$金鹰国际城营销中心$347$b6e0d441af4595569b68b4e1$\",\"连云港市$海州区$$金鹰国际花园-南门$347$ae9b574b8589a898bca822b2$\",\"连云港市$海州区$$金鹰国际花园-东门$347$bc43b05cbc34500278dbd268$\",\"连云港市$海州区$$金鹰国际花园-二期$347$1d451017d49f43f38e7727f9$\",\"连云港市$海州区$$金鹰国际花园-一期$347$af26423964cf68faf01937bf$\",\"连云港市$海州区$$金鹰国际花园-北门$347$ae103251b236bee4f4e73b2c$\",\"连云港市$海州区$$金鹰国际花园7幢$347$634b815fdf9599184207c1ef$\",\"连云港市$海州区$$金鹰国际花园-东二门$347$39313dfc2bfb2d94f50e7790$\",\"和田地区$和田市$$金鹰国际城$82$6177a61e5a21c5b14f6d4693$\"],\"t\":0}";
//        JSONArray json = JSONUtil.parseObj(body).getJSONArray("s");
//
//        List<CommunityInfo> list = Lists.newArrayListWithCapacity(json.size());
//        for (Object o : json) {
//            String[] s = ((String)o).split("\\$");
//
//            CommunityInfo info = new CommunityInfo();
//            info.setCityName(s[0]);
//            info.setDistrict(s[1]);
//            info.setCommunityName(s[3]);
//            info.setUid(s[5]);
//
//            list.add(info);
//        }
//        log.info("{}", objectMapper.writeValueAsString(list));

        //String body = "[{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园\",\"uid\":\"bbb467bd96bc0a9ccac3db61\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际城营销中心\",\"uid\":\"b6e0d441af4595569b68b4e1\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-南门\",\"uid\":\"ae9b574b8589a898bca822b2\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-东门\",\"uid\":\"bc43b05cbc34500278dbd268\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-二期\",\"uid\":\"1d451017d49f43f38e7727f9\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-一期\",\"uid\":\"af26423964cf68faf01937bf\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-北门\",\"uid\":\"ae103251b236bee4f4e73b2c\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园7幢\",\"uid\":\"634b815fdf9599184207c1ef\"},{\"cityName\":\"连云港市\",\"district\":\"海州区\",\"communityName\":\"金鹰国际花园-东二门\",\"uid\":\"39313dfc2bfb2d94f50e7790\"},{\"cityName\":\"和田地区\",\"district\":\"和田市\",\"communityName\":\"金鹰国际城\",\"uid\":\"6177a61e5a21c5b14f6d4693\"}]";

        // getCommunityBorder("bbb467bd96bc0a9ccac3db61");

        //String coordinates = "4|13273373.2070162,4085903.96828542;13273850.6528656,4086416.24268935|1-13273390.28087217,4086381.217929446,13273373.41476029,4086356.008533257,13273373.20701624,4086328.546336368,13273384.2367634,4086258.393125538,13273407.15684409,4086167.096600162,13273451.20370733,4085999.709693775,13273477.10829265,4085916.537242749,13273497.65115094,4085907.479529735,13273514.41570596,4085905.382016196,13273823.3817566,4085903.968285418,13273829.26070373,4085906.474849711,13273834.69488976,4085911.263693544,13273840.01537497,4085922.646977838,13273850.65286564,4086198.612973503,13273848.66885856,4086391.074720384,13273841.004913,4086408.1944336,13273834.45622391,4086414.158048696,13273820.14350576,4086416.24268935,13273410.4879637,4086386.812216936,13273390.28087217,4086381.217929446;";
        //List<Point> pointList = coordinateToPoints(coordinates);

        //Point point = meter2Degree("13273390.28087217", "4086381.217929446");
        //log.info("{}", objectMapper.writeValueAsString(pointList));

        // 119.23559623413306 34.60902146363141
        // log.info("{}", objectMapper.writeValueAsString(meter2DegreeLocal("13273390.28087217", "4086381.217929446")));
    }

}
