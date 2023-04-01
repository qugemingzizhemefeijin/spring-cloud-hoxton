package com.atguigu.springcloud.guava.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusTest {

    public static void main(String[] args) {
        EventBus bus = new EventBus();

        J15Observer j15 = new J15Observer();
        J20Observer j20 = new J20Observer();
        J35Observer j35 = new J35Observer();
        bus.register(j15);
        bus.register(j20);
        bus.register(j35);

        Missile a15 = new Missile("J15", 1);
        bus.post(a15);

        Missile a20 = new Missile("J20", 4);
        bus.post(a20);

        Missile a35 = new Missile("J35", 3);
        bus.post(a35);
    }

}
