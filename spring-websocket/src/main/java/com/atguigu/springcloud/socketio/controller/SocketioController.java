package com.atguigu.springcloud.socketio.controller;

import com.atguigu.springcloud.socketio.configuration.MessageEventHandler;
import com.corundumstudio.socketio.SocketIOClient;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/socketio")
public class SocketioController {

    @GetMapping("/index")
    public String index() {
        return "socketio/index";
    }

    /**
     * web socketio方式指定用户发送消息
     * @param token String
     * @return Map<String, Object>
     */
    @GetMapping(value = "/{token}")
    public @ResponseBody Map<String, Object> socketIoTest(@PathVariable("token") String token){
        SocketIOClient client = MessageEventHandler.webSocketMap.get(token);

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("token", token);
        dataMap.put("success", true);
        dataMap.put("msg", "指定给" + token + "用户发消息");

        client.sendEvent("messageevent","server data " + LocalDateTime.now().toString());
        return dataMap;
    }

    /**
     * 所有用户发送消息
     * @return Map<String, Object>
     */
    @GetMapping(value = "/send/notice")
    public @ResponseBody Map<String, Object> socketIoAll(){
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("success", true);
        dataMap.put("msg", "向所有用户发消息");

        MessageEventHandler.sendAllEvent();

        return dataMap;
    }

}
