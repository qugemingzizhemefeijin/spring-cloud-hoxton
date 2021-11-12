package com.atguigu.springcloud.springpackage.configuration;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class WsSessionManager {

    /**
     * 保存连接 session 的地方
     */
    private static final ConcurrentMap<String, WebSocketSession> SESSION_POOL = Maps.newConcurrentMap();

    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获得 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }

}
