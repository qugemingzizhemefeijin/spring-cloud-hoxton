package com.atguigu.springcloud.nettywebsocket.endpoint;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.time.LocalDateTime;

@ServerEndpoint(value = "/netty/ws/{user}", port = "9527")
@Component
@Slf4j
public class NettyWebSocketEndpoint {

    /**
     * 连接成功
     * @param session Session
     * @param headers HttpHeaders
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @PathVariable("user")String user) throws IOException {
        log.info("连接成功, sessionId = {}, user = {}", session.id().asShortText(), user);
    }

    /**
     * 连接关闭
     * @param session Session
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        log.info("连接关闭, sessionId = {}", session.id().asShortText());
    }

    /**
     * 发生错误了
     * @param session   Session
     * @param throwable Throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("onError :" + throwable.getMessage(), throwable);
    }

    /**
     * 接收到消息
     * @param session Session
     * @param message 消息内容
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("接收消息, sessionId = {}, message = {}", session.id().asShortText(), message);
        session.sendText("服务端时间：" + LocalDateTime.now().toString());
    }

}
