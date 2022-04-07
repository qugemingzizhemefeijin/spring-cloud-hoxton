package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.GrpcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrpcClientController {

    @Value("${auth.username}")
    private String username;

    @Autowired
    private GrpcClientService grpcClientService;

    @RequestMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String printMessage(@RequestParam(defaultValue = "Michael") final String name) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Input:\n")
                .append("- name: " + name + " (Changeable via URL param ?name=X)\n")
                .append("Request-Context:\n")
                .append("- auth user: " + this.username + " (Configure via application.yml)\n")
                .append("Response:\n")
                .append(this.grpcClientService.sendMessage(name));
        return sb.toString();
    }

}
