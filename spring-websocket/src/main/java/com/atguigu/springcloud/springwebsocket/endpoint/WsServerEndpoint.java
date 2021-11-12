package com.atguigu.springcloud.springwebsocket.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

// 测试地址 http://localhost:8080/websocket/normal
@ServerEndpoint("/spring/ws/{user}")
@Component
@Slf4j
public class WsServerEndpoint {

    /**
     * 连接成功
     * @param user    String
     * @param session Session
     */
    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        log.info("连接成功, sessionId = {}, user = {}", session.getId(), user);
    }

    /**
     * 连接关闭
     * @param session     Session
     * @param closeReason CloseReason
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("连接关闭, sessionId = {}，because of {}", session.getId(), closeReason);
    }

    /**
     * 接收到消息
     * @param text    String
     * @param session Session
     */
    @OnMessage
    public String onMessage(String text, Session session) throws IOException {
        log.info("接收消息, sessionId = {}, text = {}", session.getId(), text);
        return "server 发送：" + text;
    }

    @OnError
    public void onError(Throwable t) {
        log.info("onError :" + t.getMessage());
    }

}
