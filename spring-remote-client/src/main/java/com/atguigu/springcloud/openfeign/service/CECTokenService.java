package com.atguigu.springcloud.openfeign.service;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "Remote-Client", url = "http://127.0.0.1:8080")
public interface CECTokenService {

    @PostMapping("/get/token")
    QueryTokenResp queryToken(QueryTokenReq req);

}
