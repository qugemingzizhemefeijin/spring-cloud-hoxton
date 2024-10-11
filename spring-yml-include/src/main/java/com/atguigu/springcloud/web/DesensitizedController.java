package com.atguigu.springcloud.web;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.springcloud.other.desensitization.jackson.UserDto;
import com.atguigu.springcloud.other.desensitization.returnvalue.LogDto;
import com.atguigu.springcloud.other.desensitization.returnvalue.Result;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

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
        Result<LogDto> ok = Result.ok();
        ok.setData(dto);
        return ok;
    }

    @GetMapping(value = "/index")
    public Result<UserDto> task(){
        Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);

        // task.task();
        UserDto dto = new UserDto();
        dto.setUsername(faker.name().fullName());
        dto.setCellphone("13566556666");
        dto.setAddress(faker.address().fullAddress());
        dto.setEmail(faker.internet().emailAddress("ass22133"));
        dto.setAge(faker.random().nextInt(10, 100));
        log.info("user info {}", dto);
        log.info("user info {}", JSONObject.toJSONString(dto));

        Result<UserDto> ret = Result.ok();
        ret.setData(dto);

        return ret;
    }

}
