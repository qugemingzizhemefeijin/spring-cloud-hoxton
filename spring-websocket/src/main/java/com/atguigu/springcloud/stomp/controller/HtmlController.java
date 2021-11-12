package com.atguigu.springcloud.stomp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stomp/socket")
public class HtmlController {

    @GetMapping("/index")
    public String index() {
        return "stomp/index";
    }

    @GetMapping("/info")
    public String info() {
        return "stomp/info";
    }

    @GetMapping("/msg/user1")
    public String toMessage() {
        return "stomp/user1";
    }

    @GetMapping("/msg/user2")
    public String toMessaget2() {
        return "stomp/user2";
    }

}
