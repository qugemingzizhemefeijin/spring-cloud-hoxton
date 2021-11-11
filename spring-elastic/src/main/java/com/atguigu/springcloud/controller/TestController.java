package com.atguigu.springcloud.controller;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public Map<String, Object> helloWorld() {
        Map<String, Object> m = Maps.newHashMap();
        m.put("code", 0);
        m.put("msg", "success");
        return m;
    }

}
