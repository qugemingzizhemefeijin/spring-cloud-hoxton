package com.atguigu.springcloud.openfeign.controller;

import com.atguigu.springcloud.openfeign.decrypt.CECDecoder;
import com.atguigu.springcloud.openfeign.decrypt.CECEncoder;
import com.atguigu.springcloud.openfeign.domain.CECRequest;
import com.atguigu.springcloud.openfeign.domain.CECResponse;
import com.atguigu.springcloud.openfeign.domain.QueryTokenReq;
import com.atguigu.springcloud.openfeign.service.CECTokenService;
import com.atguigu.springcloud.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TokenController {

    @Resource
    private CECDecoder decoder;

    @Resource
    private CECEncoder encoder;

    @Resource
    private CECTokenService tokenService;

    @PostMapping("/get/token")
    public @ResponseBody CECResponse<String> get(@RequestBody CECRequest<String> req) {
        String body = decoder.decryptStr(req.getData());
        log.info("body = {}", body);

        QueryTokenReq r = JsonUtil.toObject(body, QueryTokenReq.class);

        String ret = encoder.getEncrypt(tokenService.queryToken(r));

        CECResponse<String> vvv = new CECResponse<>();
        vvv.setData(ret);
        vvv.setMsg("success");
        vvv.setRet(0);
        return vvv;
    }

}
