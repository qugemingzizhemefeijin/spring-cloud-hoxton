package com.atguigu.springcloud.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class CallbackReq implements Serializable {

    private static final long serialVersionUID = 1084260088305503922L;

    private String username;

    private String address;

    private String userId;

}
