package com.atguigu.springcloud.turf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/turf")
public class TurfController {

    @GetMapping("/index")
    public String index() {
        return "turf/index";
    }

}
