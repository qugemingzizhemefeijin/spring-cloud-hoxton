package com.atguigu.springcloud.web;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.config.desensitized.Result;
import com.atguigu.springcloud.other.desensitized.LogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class DesensitizedController {

    @GetMapping(value = "/log")
    public Result<LogDto> queryLog(){
        LogDto dto = new LogDto();
        dto.setName("张缇娜");
        dto.setIdCard("411024199112250937");
        dto.setCellphone("12314354762");
        dto.setMoney("23.8");
        dto.setRemark("测试内容");
        log.info("log is {}", JSONObject.toJSONString(dto));
        Result ok = Result.ok();
        ok.setData(dto);
        return ok;
    }

}
