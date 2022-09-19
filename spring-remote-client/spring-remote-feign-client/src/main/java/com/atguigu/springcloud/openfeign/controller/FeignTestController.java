package com.atguigu.springcloud.openfeign.controller;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import com.atguigu.springcloud.openfeign.rpc.CECTokenRPC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class FeignTestController {

    @Resource
    private CECTokenRPC tokenRPC;

    @GetMapping("/feign/test")
    public QueryTokenResp get(@RequestParam String operatorId, @RequestParam String secret) {
        QueryTokenReq r = new QueryTokenReq();
        r.setOperatorID(operatorId);
        r.setOperatorSecret(secret);

        return tokenRPC.queryToken(r);
    }

}
