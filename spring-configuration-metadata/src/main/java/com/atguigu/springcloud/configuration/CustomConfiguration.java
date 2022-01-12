package com.atguigu.springcloud.configuration;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(CustomProperties.class)
public class CustomConfiguration {

    @Resource
    private CustomProperties customProperties;

    @Bean
    public CustomBean buildCustomBean() {
        CustomBean customBean = new CustomBean();
        customBean.setJson(JSONObject.toJSONString(customProperties));

        return customBean;
    }

}
