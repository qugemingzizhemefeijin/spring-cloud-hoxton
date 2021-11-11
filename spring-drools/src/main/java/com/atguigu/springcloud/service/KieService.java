package com.atguigu.springcloud.service;

import com.atguigu.springcloud.vo.ReqAndResult;

import java.util.List;

public interface KieService {

    void loadRules(String ruleCode);

    void execute(Object object);

    List<Object> executeReturnResult(ReqAndResult object);

}
