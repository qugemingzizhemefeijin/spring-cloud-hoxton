package com.atguigu.springcloud.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReqAndResult implements Serializable {

    private String ruleCode;

    private Object req;

    private List<Object> result = new ArrayList<>();

    public void addResult(Object obj){
        result.add(obj);
    }

    public ReqAndResult() {

    }

    public ReqAndResult(String ruleCode) {
        this.ruleCode = ruleCode;
    }
    public ReqAndResult(Object req) {
        this.req = req;
    }
    public ReqAndResult(String ruleCode, Object req) {
        this.ruleCode = ruleCode;
        this.req = req;
    }
}
