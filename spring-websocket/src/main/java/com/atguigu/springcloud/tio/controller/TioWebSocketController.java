package com.atguigu.springcloud.tio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tio.core.Tio;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

@Controller
@RequestMapping("/tio")
public class TioWebSocketController {

    @Autowired
    private TioWebSocketServerBootstrap bootstrap;

    @GetMapping("/index")
    public String index() {
        return "tio/websocket";
    }

    @GetMapping("/msg")
    public void pushMessage(String msg) {
        if (StrUtil.isEmpty(msg)) {
            msg = "hello tio websocket spring boot starter";
        }
        Tio.sendToAll(bootstrap.getServerTioConfig(), WsResponse.fromText(msg, "utf-8"));
    }

}
