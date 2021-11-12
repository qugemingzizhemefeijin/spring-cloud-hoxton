package com.atguigu.springcloud.springpackage.configuration;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class MyInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手开始");
        // 获得请求参数
        Map<String, String> paramMap = Splitter.on("&").withKeyValueSeparator("=").split(request.getURI().getQuery());
        String uid = paramMap.get("token");
        if (uid != null && uid.length() >  0) {
            // 放入属性域
            attributes.put("token", uid);
            log.info("用户 token " + uid + " 握手成功！");
            return true;
        }
        log.info("用户登录已失效");
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成");
    }

}
