package com.atguigu.springcloud.nettywebsocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/netty/websocket")
public class NettyWebsocketController {

    @GetMapping("/index")
    public String normal() {
        //输出日志文件
        return "nettywebsocket/websocket";
    }

}
