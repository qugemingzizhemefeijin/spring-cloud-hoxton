package com.atguigu.springcloud.springwebsocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequestMapping("/websocket")
public class SpringWebSocketController {

    @GetMapping("/normal")
    public String normal() {
        //输出日志文件
        log.info("websocket page");
        return "websocket/websocket2";
    }

    @RequestMapping("/sockjs/info")
    @ResponseBody
    public String sendMsg(HttpServletResponse response) {
        String str = "{\"name\":\"aaa\",\"age\":22}";
        log.info(str);
        response.setHeader("Access-Control-Allow-Origin", "*");
        return str;
    }

}
