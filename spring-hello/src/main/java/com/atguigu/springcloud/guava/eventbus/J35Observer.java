package com.atguigu.springcloud.guava.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public class J35Observer {

    // 标记当前订阅者是线程安全的，支持并发接收消息
    @AllowConcurrentEvents
    @Subscribe
    public void attack(Missile missile) {
        if (missile.getAttack().equalsIgnoreCase("J35")) {
            System.out.println("J35 出击 >>>>>>>>>>>>> Kill Plane");
            System.out.println(missile.toString());
        }
    }

}
