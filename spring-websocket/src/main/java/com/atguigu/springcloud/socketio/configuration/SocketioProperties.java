package com.atguigu.springcloud.socketio.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "socketio.websocket")
public class SocketioProperties {

    private String host;

    private int port;

    private KeyStore keyStore;

    // 连接数大小
    private int workCount;

    // 允许客户请求
    private boolean allowCustomRequests;

    // 协议升级超时时间(毫秒)，默认10秒，HTTP握手升级为ws协议超时时间
    private int upgradeTimeout;

    // Ping消息超时时间(毫秒)，默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
    private int pingTimeout;

    // Ping消息间隔(毫秒)，默认25秒。客户端向服务器发送一条心跳消息间隔
    private int pingInterval;

    // 设置HTTP交互最大内容长度
    private int maxHttpContentLength;

    // 设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
    private int maxFramePayloadLength;

    @Data
    static class KeyStore {

        private String password;

        private String filePath;

    }

}
