package com.atguigu.springcloud.tio.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TioWebSocketHandler implements IWsMsgHandler {

    /**
     * 握手
     *
     * @param httpRequest    HttpRequest
     * @param httpResponse   HttpResponse
     * @param channelContext ChannelContext
     * @return HttpResponse
     * @throws Exception
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        log.info("客户端请求握手");
        return httpResponse;
    }

    /**
     * 握手成功
     *
     * @param httpRequest    HttpRequest
     * @param httpResponse   HttpResponse
     * @param channelContext ChannelContext
     * @throws Exception
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        log.info("握手成功");
    }

    /**
     * 接收二进制文件
     *
     * @param wsRequest      WsRequest
     * @param bytes          byte[]
     * @param channelContext ChannelContext
     * @return Object
     * @throws Exception
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 断开连接
     *
     * @param wsRequest      WsRequest
     * @param bytes          byte[]
     * @param channelContext ChannelContext
     * @return Object
     * @throws Exception
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        log.info("关闭连接");
        return null;
    }

    /**
     * 接收消息
     *
     * @param wsRequest      WsRequest
     * @param s              String
     * @param channelContext ChannelContext
     * @return Object
     * @throws Exception
     */
    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
        log.info("接收文本消息:" + s);
        return "success, server Time = " + LocalDateTime.now().toString();
    }

}
