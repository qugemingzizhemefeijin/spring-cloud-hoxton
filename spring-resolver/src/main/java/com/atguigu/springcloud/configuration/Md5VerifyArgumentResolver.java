package com.atguigu.springcloud.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.utils.MD5;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@AllArgsConstructor
@Component
@Slf4j
//实现 HandlerMethodArgumentResolver 接口
public class Md5VerifyArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 此方法用来判断本次请求的接口是否需要解析参数，
     * 如果需要返回 true，然后调用下面的 resolveArgument 方法,
     * 如果不需要返回 false
     *
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Md5Verify.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Md5Verify parameterAnnotation = parameter.getParameterAnnotation(Md5Verify.class);
        if (parameterAnnotation == null || !parameterAnnotation.verifySign()) {
            return mavContainer.getModel();
        }

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;

        if ("application/json".equals(request.getContentType())) {
            InputStream inputStream = request.getInputStream();

            JSONObject json = JSON.parseObject(new String(IOUtils.toByteArray(inputStream)));
            String sign = request.getHeader("sign");

            //对参数进行处理并验签的逻辑
            if(!this.verify(json, sign)) {
                throw new RuntimeException("verify is error");
            }

            return json.toJavaObject(parameter.getParameterType());
        }

        return mavContainer.getModel();
    }

    private boolean verify(JSONObject json, String sign) throws UnsupportedEncodingException {
        String s = sign(json);
        log.info("s = {}, sign = {}", s, sign);

        return s.equals(sign);
    }

    private String sign(JSONObject json) throws UnsupportedEncodingException {
        Map<String, String> params = Maps.newTreeMap();
        for (Map.Entry<String, Object> me : json.entrySet()) {
            params.put(me.getKey(), URLDecoder.decode(me.getValue().toString(), "UTF-8"));
        }

        StringBuilder buf = new StringBuilder();
        for(Map.Entry<String, String> me : params.entrySet()) {
            buf.append(me.getKey()).append(me.getValue());
        }

        return MD5.encode(buf.toString());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Md5VerifyArgumentResolver resolver = new Md5VerifyArgumentResolver();
        JSONObject callback = JSONObject.parseObject("{\"username\":\"小橙子\",\"address\":\"北京\",\"userId\":\"123\"}");
        log.info(resolver.sign(callback));
    }

}
