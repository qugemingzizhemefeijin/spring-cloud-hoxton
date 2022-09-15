package com.atguigu.springcloud.openfeign.service;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "CLOUD-PAYMENT-SERVICE")
public interface CECTokenService {

    QueryTokenResp queryToken(QueryTokenReq req);

}
