package com.atguigu.springcloud.event;

import org.springframework.context.ApplicationEvent;

public class SmartEvent extends ApplicationEvent {

    private String msg;

    public SmartEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
