package com.atguigu.springcloud.openfeign.service.impl;

import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.domain.QueryTokenResp;
import com.atguigu.springcloud.openfeign.service.CECTokenService;
import org.springframework.stereotype.Service;

@Service
public class CECTokenServiceImpl implements CECTokenService {

    @Override
    public QueryTokenResp queryToken(QueryTokenReq req) {
        QueryTokenResp resp = new QueryTokenResp();
        resp.setNickname("Hello" + req.getOperatorID());
        resp.setUsername("World");
        return resp;
    }

}
