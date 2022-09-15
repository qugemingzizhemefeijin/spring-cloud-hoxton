package com.atguigu.springcloud.openfeign.decrypt;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.atguigu.springcloud.openfeign.CECOperatorProperties;
import com.atguigu.springcloud.openfeign.domain.CECResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CECDecoder extends SpringDecoder {

    private final AES aes;

    public CECDecoder(ObjectFactory<HttpMessageConverters> messageConverters, CECOperatorProperties properties) {
        super(messageConverters);
        this.aes = new AES(Mode.CBC, Padding.PKCS5Padding, properties.getDataSecret().getBytes(), properties.getDataIv().getBytes());
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        CECResponse<String> resp = this.getCECResponse(response);
        // TODO 应该做对应的异常判断然后抛出异常
        String json = this.aes.decryptStr(resp.getData());
        Response newResp = response.toBuilder().body(json, StandardCharsets.UTF_8).build();
        return super.decode(newResp, type);
    }

    private CECResponse<String> getCECResponse(Response response) throws IOException {
        try (InputStream inputStream = response.body().asInputStream()) {
            String json = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            TypeReference<CECResponse<String>> reference = new TypeReference<CECResponse<String>>() {
            };
            return JSONUtil.toBean(json, reference.getType(), true);
        }
    }
}
