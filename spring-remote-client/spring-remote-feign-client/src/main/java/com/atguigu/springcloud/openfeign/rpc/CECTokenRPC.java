package com.atguigu.springcloud.openfeign.rpc;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "spring-remote-feign-client", url = "http://127.0.0.1:8090")
public interface CECTokenRPC {

    @PostMapping("/get/token")
    QueryTokenResp queryToken(QueryTokenReq req);

}
