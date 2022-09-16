package com.atguigu.springcloud.openfeign.service;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;

public interface CECTokenService {

    QueryTokenResp queryToken(QueryTokenReq req);

}
