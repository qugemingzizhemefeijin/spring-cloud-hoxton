package com.atguigu.springcloud.event;

import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {

    private String msg;

    public CustomEvent(Object source, String msg) {
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
