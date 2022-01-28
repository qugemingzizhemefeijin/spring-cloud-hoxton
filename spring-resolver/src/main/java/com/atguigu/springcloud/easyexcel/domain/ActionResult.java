package com.atguigu.springcloud.easyexcel.domain;

import lombok.Data;

@Data
public class ActionResult<T> {

    private int code;

    private String message;

    private T data;

}
