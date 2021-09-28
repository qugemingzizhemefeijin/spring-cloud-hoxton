package com.atguigu.springcloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.event.CustomEvent;
import com.atguigu.springcloud.event.SmartEvent;
import com.atguigu.springcloud.service.EventFireService;
import com.atguigu.springcloud.service.TestService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private EventFireService eventFireService;

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public @ResponseBody JSONObject test() {
        log.info("test invoke start");
        testService.a();

        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("msg", "成功");
        return json;
    }

    @RequestMapping({"/", "/index"})
    public String indexModel(Model model) {
        model.addAttribute("msg", "错误信息");
        model.addAttribute("title", "设置标题");
        model.addAttribute("bb", 1);

        List<String> list = Lists.newArrayList("你","好","北","京");
        model.addAttribute("list", list);

        return "index";
    }

    @GetMapping("/fire/event1")
    public @ResponseBody JSONObject fireEvent1() {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("msg", "成功");

        eventFireService.fireEvent(new CustomEvent("Hello", "World"));

        return json;
    }

    @GetMapping("/fire/event2")
    public @ResponseBody JSONObject fireEvent2() {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("msg", "成功");

        eventFireService.fireEvent(new SmartEvent("Hello", "World"));

        return json;
    }

}
