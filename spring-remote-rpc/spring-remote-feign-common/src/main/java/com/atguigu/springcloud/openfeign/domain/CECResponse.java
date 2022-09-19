package com.atguigu.springcloud.openfeign.domain;

import lombok.Data;

@Data
public class CECResponse<T> {

    private Integer Ret;

    private T Data;

    private String Msg;

}
