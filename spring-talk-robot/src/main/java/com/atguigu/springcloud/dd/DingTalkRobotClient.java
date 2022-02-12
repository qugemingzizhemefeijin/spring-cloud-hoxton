package com.atguigu.springcloud.dd;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 钉钉机器人客户端
 */
@Slf4j
public class DingTalkRobotClient {

    private static final String SECRET = "asasdadad";

    private static final String WEB_HOOK = "https://oapi.dingtalk.com/robot/send?access_token=asdasdasd";

    //发送超时时间10s
    private static final int TIME_OUT = 10000;

    /**
     * 钉钉机器人文档地址https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
     *
     * @param content    发送内容
     * @param mobileList 通知具体人的手机号码列表 （可选）
     * @return String
     */
    public static String sendMsg(String content, List<String> mobileList) {
        try {
            String url = WEB_HOOK;
            //钉钉机器人地址（配置机器人的webhook）
            long timestamp = System.currentTimeMillis();
            String sign = getSign(timestamp, SECRET);
            url += "&timestamp=" + timestamp + "&sign=" + sign;

            log.info("url:" + url);
            //是否通知所有人
            boolean isAtAll = !CollectionUtils.isEmpty(mobileList);
            //组装请求内容
            String reqStr = buildReqStr(content, isAtAll, mobileList);
            //推送消息（http请求）
            String result = postJson(url, reqStr);
            log.info("推送结果result == " + result);
            return result;
        } catch (Exception e) {
            log.info("发送群通知异常", e);
            return null;
        }
    }

    /**
     * 组装请求报文
     * 发送消息类型 text
     *
     * @param content    消息内容
     * @param isAtAll    是否发送给所有人
     * @param mobileList 指定发送人手机列表
     * @return String
     */
    private static String buildReqStr(String content, boolean isAtAll, List<String> mobileList) {
        //消息内容
        Map<String, String> contentMap = Maps.newHashMap();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = Maps.newHashMap();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);
        Map<String, Object> reqMap = Maps.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        return JSON.toJSONString(reqMap);
    }

    private static String postJson(String url, String reqStr) {
        String body = null;
        try {
            body = HttpRequest.post(url).body(reqStr).timeout(TIME_OUT).execute().body();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return body;
    }

    /**
     * 自定义机器人获取签名
     * 创建机器人时选择加签获取secret以SE开头
     *
     * @param timestamp 时间戳
     * @param secret    签名秘钥
     * @return String
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    private static String getSign(Long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        log.info("sign:{}", sign);
        return sign;
    }

    public static void main(String[] args) {
        List<String> mobileList = Lists.newArrayList();
        mobileList.add("13683211004");
        DingTalkRobotClient.sendMsg("小哥哥，你好", null);
    }

}
