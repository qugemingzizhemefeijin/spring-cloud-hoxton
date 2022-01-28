package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.configuration.Md5Verify;
import com.atguigu.springcloud.configuration.ResultBody;
import com.atguigu.springcloud.easyexcel.domain.CallbackReq;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    /**
     * @param params CallbackReq
     * @return String
     */
    @PostMapping("/verify")
    @ResponseBody
    public String callback(@Md5Verify CallbackReq params) {
        log.info("receive verify callback req = {}", params);
        //业务逻辑处理

        return "success";
    }

    /**
     * @param params CallbackReq
     * @return String
     */
    @PostMapping("/no")
    @ResponseBody
    public String noVerify(@RequestBody CallbackReq params) {
        log.info("receive no verify callback req = {}", params);
        //业务逻辑处理

        return "success";
    }

    @GetMapping("/getJson")
    @ResponseBody
    public Map<String, Object> toJson() {
        return ImmutableMap.of("username", "test", "password", "123456", "sex", 1);
    }

    @GetMapping("/getContent")
    @ResultBody
    public Map<String, Object> toBodyJson() {
        return ImmutableMap.of("username", "smallorange", "password", "123456", "sex", 1);
    }

}
