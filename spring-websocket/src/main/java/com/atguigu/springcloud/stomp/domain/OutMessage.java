package com.atguigu.springcloud.stomp.domain;

import lombok.Data;

@Data
public class OutMessage {

    private String content;

    public OutMessage() {

    }

    public OutMessage(String content) {
        this.content = content;
    }

}
