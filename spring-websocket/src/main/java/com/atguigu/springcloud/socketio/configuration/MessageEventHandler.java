package com.atguigu.springcloud.socketio.configuration;

import com.atguigu.springcloud.socketio.domain.MessageInfo;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * 消息事件，作为后端与前台交互
 */
@Component
@Slf4j
public class MessageEventHandler {

    public static SocketIOServer socketIoServer;

    public static final ConcurrentMap<String, UUID> uuidClient = Maps.newConcurrentMap();

    public static final ConcurrentMap<String,SocketIOClient> webSocketMap = Maps.newConcurrentMap();

    public MessageEventHandler(SocketIOServer server) {
        socketIoServer = server;
    }

    /**
     * 客户端连接的时候触发，前端js触发：socket = io.connect("http://192.168.9.209:9092");
     * @param client SocketIOClient
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        uuidClient.put(token, client.getSessionId());

        // 后续可以指定mac地址向客户端发送消息
        webSocketMap.put(token,client);
        //socketIoServer.getClient(client.getSessionId()).sendEvent("message", "back data");
        log.info("客户端: {} 已连接,token = {}", client.getSessionId(), token);
    }

    /**
     * 客户端关闭连接时触发：前端js触发：socket.disconnect();
     * @param client SocketIOClient
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        log.info("客户端: {} 断开连接, token = {}", client.getSessionId(), token);

        if(token != null && token.length() > 0) {
            uuidClient.remove(token);
            webSocketMap.remove(token);
        }
    }

    /**
     * 自定义消息事件，客户端js触发：socket.emit('messageevent', {msgContent: msg}); 时触发
     * 前端js的 socket.emit("事件名","参数数据")方法，是触发后端自定义消息事件的时候使用的,
     * 前端js的 socket.on("事件名",匿名函数(服务器向客户端发送的数据))为监听服务器端的事件
     * @param client　客户端信息
     * @param request 请求信息
     * @param data　客户端发送数据{msgContent: msg}
     */
    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("发来消息：" + data);
        // 服务器端向该客户端发送消息
        // socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "你好 data");
        // client.sendEvent("messageevent","我是服务器都安发送的信息");

        // 同时广播给其他的客户端
        try {
            client.getNamespace().getBroadcastOperations().sendEvent("messageevent", "服务端转发消息：" + data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 接收图片信息
     * @param client  SocketIOClient
     * @param request AckRequest
     * @param data    byte[]
     */
    @OnEvent(value = "msg")
    public void onImageEvent(SocketIOClient client, AckRequest request, byte[] data) {
        // 将图片原样返回
        client.sendEvent("msg", (Object) data);
    }

    /**
     * 向所有客户端推消息
     */
    public static void sendAllEvent() {
        String time = LocalDateTime.now().toString();
        for (UUID clientId : uuidClient.values()) {
            if (socketIoServer.getClient(clientId) == null) {
                continue;
            }
            socketIoServer.getClient(clientId).sendEvent("messageevent", "服务端当前时间:" + time, 1);
        }
    }

    /**
     * 断开所有的链接
     */
    public void invalidWebSocket() {
        for (UUID clientId : uuidClient.values()) {
            //遍历该房间内的所有uuid,获取socketClient并断开连接
            SocketIOClient socketIOClient = socketIoServer.getClient(clientId);
            if (socketIOClient != null) {
                socketIOClient.disconnect();
            }
        }
    }

}
