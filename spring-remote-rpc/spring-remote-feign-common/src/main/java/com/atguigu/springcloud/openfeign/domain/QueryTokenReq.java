package com.atguigu.springcloud.openfeign.domain;

import lombok.Data;

@Data
public class QueryTokenReq {

    private String operatorID;

    private String operatorSecret;

}
