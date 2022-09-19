package com.atguigu.springcloud.openfeign;

import com.atguigu.springcloud.openfeign.decrypt.CECDecoder;
import com.atguigu.springcloud.openfeign.decrypt.CECEncoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration(proxyBeanMethods = false) 配置该类配置类，并且不会在RootApplicationContext当中注册，只会在使用的时候才会进行相关配置。
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CECOperatorProperties.class)
public class CECFeignClientConfig implements RequestInterceptor {

    @Autowired
    private CECOperatorProperties properties;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Encoder encoder(){
        return new CECEncoder(messageConverters, properties);
    }
    @Bean
    public Decoder decoder(){
        return new CECDecoder(messageConverters, properties);
    }

    @Override
    public void apply(RequestTemplate template) {
        // TODO 添加Token
    }

}
