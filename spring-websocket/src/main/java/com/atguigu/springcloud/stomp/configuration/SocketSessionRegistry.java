package com.atguigu.springcloud.stomp.configuration;

import com.google.common.collect.Maps;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class SocketSessionRegistry {

    private final ConcurrentMap<String, Set<String>> userSessionIds = Maps.newConcurrentMap();

    private final Object lock = new Object();

    /**
     * 获取sessionId
     */
    public Set<String> getSessionIds(String user) {
        Set<String> set = this.userSessionIds.get(user);
        return set != null ? set : Collections.emptySet();
    }

    /**
     * 获取所有session
     */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    /**
     * register session
     */
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        synchronized (this.lock) {
            Set<String> set = this.userSessionIds.get(user);
            if (set == null) {
                set = new CopyOnWriteArraySet<>();
                this.userSessionIds.put(user, set);
            }
            set.add(sessionId);
        }
    }

    public void unregisterSessionId(String userName, String sessionId) {
        Assert.notNull(userName, "User Name must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        synchronized (this.lock) {
            Set<String> set = this.userSessionIds.get(userName);
            if (set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(userName);
            }

        }
    }

}
