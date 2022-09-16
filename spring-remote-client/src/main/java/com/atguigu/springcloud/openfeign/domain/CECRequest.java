package com.atguigu.springcloud.openfeign.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CECRequest<T> {

    @JsonProperty("OperatorID")
    private String operatorID;

    @JsonProperty("Data")
    private T data;

    @JsonProperty("TimeStamp")
    private String timeStamp;

    @JsonProperty("Seq")
    private String seq;

    @JsonProperty("Sig")
    private String sig;

}
