package com.atguigu.springcloud.stomp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.List;

public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        List<String> list = sha.getNativeHeader("username");
        if(list != null && !list.isEmpty()) {
            String agentId = list.get(0);
            String sessionId = sha.getSessionId();
            webAgentSessionRegistry.registerSessionId(agentId, sessionId);
        }
    }

}
