package com.atguigu.springcloud.springwebsocket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

// 开启配置 spring boot 才能去扫描后面的关于 websocket 的注解
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }

}
