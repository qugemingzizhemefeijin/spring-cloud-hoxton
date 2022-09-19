package com.atguigu.springcloud.openfeign.decrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import com.atguigu.springcloud.openfeign.CECOperatorProperties;
import com.atguigu.springcloud.openfeign.domain.CECRequest;
import com.atguigu.springcloud.utils.JsonUtil;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class CECEncoder extends SpringEncoder {

    private final CECOperatorProperties properties;

    private final HMac mac;

    private final AES aes;

    public CECEncoder(ObjectFactory<HttpMessageConverters> messageConverters, CECOperatorProperties properties) {
        super(messageConverters);
        this.properties = properties;
        this.mac = new HMac(HmacAlgorithm.HmacMD5, properties.getSigSecret().getBytes(StandardCharsets.UTF_8));
        this.aes = new AES(Mode.CBC, Padding.PKCS5Padding, properties.getDataSecret().getBytes(), properties.getDataIv().getBytes());
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        // 数据加密
        CECRequest<String> req = this.encrypt(requestBody);
        super.encode(req, CECRequest.class.getGenericSuperclass(), request);
    }

    public CECRequest<String> encrypt(Object requestBody) {
        // 数据加密
        String data = this.getEncrypt(requestBody);
        CECRequest<String> req = new CECRequest<>();
        req.setData(data);
        req.setSeq("0001");
        req.setTimeStamp(DatePattern.PURE_DATETIME_FORMAT.format(new Date()));
        req.setOperatorID(properties.getOperatorID());
        // 签名计算
        String sig = this.getSig(req);
        req.setSig(sig.toUpperCase());
        return req;
    }

    public String getEncrypt(Object requestBody) {
        String json = JsonUtil.toJson(requestBody);
        return Base64.encode(aes.encrypt(json.getBytes()));
    }

    private String getSig(CECRequest<String> req) {
        String str = req.getOperatorID() + req.getData() + req.getTimeStamp() + req.getSeq();
        return mac.digestHex(str);
    }

}
