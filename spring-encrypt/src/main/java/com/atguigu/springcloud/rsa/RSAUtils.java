package com.atguigu.springcloud.rsa;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.atguigu.springcloud.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class RSAUtils {

    public static void main(String[] args) {
        RSA rsa = new RSA();
        //获得私钥
        PrivateKey privateKey = rsa.getPrivateKey();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        //获得公钥
        PublicKey publicKey = rsa.getPublicKey();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        log.info("pub key :\n{}", publicKeyBase64);
        log.info("pri key :\n{}", privateKeyBase64);

        // 椭圆曲线算法
        // https://www.javajike.com/book/hutool/chapter8/25c8d149b1ff441ef583c3c5398f59fd.html

        JSONObject resp = new JSONObject();
        resp.put("code", "0");
        resp.put("name", "老王");
        resp.put("age", "12");
        resp.put("address", "中国北京市东城区");
        // 按照字典排序
        String str = JSONObject.toJSONString(resp, SerializerFeature.MapSortField);
        // 加密 使用服务方的公钥进行加密
        String encrypt = RSAUtils.encrypt(str, publicKeyBase64);
        // 签名 对加密报文使用客户端私钥进行签名
        String sign = RSAUtils.sign(encrypt, privateKeyBase64);

        System.out.println("sign = " + sign);

        // 验证签名
        boolean verify = RSAUtils.verify(encrypt, sign, publicKeyBase64);
        System.out.println("verify = " + verify);

        if (verify) {
            // 解密报文
            String decrypt = RSAUtils.decrypt(encrypt, privateKeyBase64);
            JSONObject jsonObject1 = JSONObject.parseObject(decrypt, Feature.OrderedField);
            log.info("验签通过， 解密服务端报文 \n{}", jsonObject1.toString(SerializerFeature.PrettyFormat));
        }
    }

    /**
     * 签名算法 SHA1withRSA
     */
    private final static SignAlgorithm algorithm = SignAlgorithm.MD5withRSA;

    /**
     * 加密
     */
    public static String encrypt(String str, String pubKey) {
        RSA pubRsa = new RSA(null, pubKey);
        byte[] encryptedData = pubRsa.encrypt(str.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 签名
     */
    public static String sign(String params, String priKey) {
        Sign sign = new Sign(algorithm, priKey, null);
        return Base64.encodeBase64String(sign.sign(params.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密
     */
    public static String decrypt(String resp, String priKey) {
        RSA rsa = new RSA(priKey, null);
        return rsa.decryptStr(resp, KeyType.PrivateKey);
    }

    /**
     * 验签
     */
    public static boolean verify(String data, String signVal, String pubKey) {
        Sign sign = new Sign(algorithm, null, pubKey);
        return sign.verify(data.getBytes(StandardCharsets.UTF_8), Base64.decodeBase64(signVal.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * 使用对方公钥加密
     */
    public static String encrypt(Map<String, Object> bizMap, String pubKey) {
        TreeMap<String, Object> sort = MapUtil.sort(bizMap);
        String sortJson = JsonUtil.toJson(sort);
        RSA pubRsa = new RSA(null, pubKey);
        byte[] encryptedData = pubRsa.encrypt(sortJson.getBytes(StandardCharsets.UTF_8), KeyType.PublicKey);
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 己方私钥加签
     * 合作方收到请求后，用我方公钥验签
     */
    public static String sign(Map<String, Object> params, String priKey) {
        TreeMap<String, Object> sort = MapUtil.sort(params);
        String paramsJson = JsonUtil.toJson(sort);
        Sign sign = new Sign(algorithm, priKey, null);
        return Base64.encodeBase64String(sign.sign(paramsJson.getBytes(StandardCharsets.UTF_8)));
    }

    // /**
    //  * 对方使用公钥加密，需要使用己方私钥解密
    //  */
    // public static String decrypt(String resp, String priKey) {
    //     RSA rsa = new RSA(priKey, null);
    //     Map<String, Object> map1 = GsonUtil.fromJson2Map(resp);
    //     return rsa.decryptStr(map1.get("bizContent").toString(), KeyType.PrivateKey);
    // }

    /**
     * 验签
     */
    public static boolean verify(Map<String, Object> respMap, String pubKey) {
        String signValue = (String) respMap.get("sign");
        respMap.remove("sign");
        String s = JsonUtil.toJson(respMap);
        Sign sign = new Sign(algorithm, null, pubKey);
        return sign.verify(s.getBytes(StandardCharsets.UTF_8), Base64.decodeBase64(signValue.getBytes(StandardCharsets.UTF_8)));
    }

}
