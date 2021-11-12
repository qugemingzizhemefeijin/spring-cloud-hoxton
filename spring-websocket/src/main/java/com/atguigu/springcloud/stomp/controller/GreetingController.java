package com.atguigu.springcloud.stomp.controller;

import com.atguigu.springcloud.stomp.configuration.SocketSessionRegistry;
import com.atguigu.springcloud.stomp.domain.InMessage;
import com.atguigu.springcloud.stomp.domain.OutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/stomp/socket/msg")
public class GreetingController {

    @Autowired
    private SocketSessionRegistry webAgentSessionRegistry;
    /**
     * 消息发送工具
     */
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/change-notice")
    @SendTo("/topic/notice")
    public String greeting(String value) {
        return value;
    }

    @GetMapping(value = "/sendcommuser")
    public @ResponseBody
    OutMessage SendToCommUserMessage(HttpServletRequest request) {
        List<String> keys = new ArrayList<>(webAgentSessionRegistry.getAllSessionIds().keySet());
        Date date = new Date();
        keys.forEach(x -> {
            String sessionId = webAgentSessionRegistry.getSessionIds(x).stream().findFirst().get();
            template.convertAndSendToUser(sessionId, "/topic/greetings", new OutMessage("commmsg：allsend, " + "send  comm" + date.getTime() + "!"), createHeaders(sessionId));
        });
        return new OutMessage("sendcommuser, " + new Date() + "!");
    }

    /**
     * 同样的发送消息   只不过是ws版本  http请求不能访问 ，根据用户key发送消息
     *
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/msg/hellosingle")
    public void greeting2(InMessage message) throws Exception {
        Map<String, String> params = new HashMap<>(1);
        params.put("test", "test");
        //这里没做校验

        for (String sessionId: webAgentSessionRegistry.getSessionIds(message.getReceiver())) {
            template.convertAndSendToUser(sessionId, "/topic/greetings", new OutMessage(message.getSender() + ": " + message.getMsg()), createHeaders(sessionId));
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
