package com.atguigu.springcloud.springpackage.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class SpringPackageWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private HttpAuthHandler httpAuthHandler;

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(httpAuthHandler, "myWS")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*"); // 这个是关闭跨域校验，方便本地调试，线上推荐打开
    }

}
