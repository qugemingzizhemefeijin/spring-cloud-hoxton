package com.atguigu.springcloud.nettywebsocket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

@Configuration
public class NettyWebsocketConfiguration {

    // 交给Spring容器，表示要开启WebSocket功能
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
