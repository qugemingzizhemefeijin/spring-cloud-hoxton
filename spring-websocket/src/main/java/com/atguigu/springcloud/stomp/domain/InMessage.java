package com.atguigu.springcloud.stomp.domain;

import lombok.Data;

@Data
public class InMessage {

    private String msg;

    private String sender;

    private String receiver;

}
