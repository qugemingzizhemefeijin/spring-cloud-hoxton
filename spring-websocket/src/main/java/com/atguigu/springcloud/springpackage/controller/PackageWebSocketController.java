package com.atguigu.springcloud.springpackage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/package")
public class PackageWebSocketController {

    @GetMapping("/normal")
    public String normal() {
        return "packages/websocket";
    }

}
