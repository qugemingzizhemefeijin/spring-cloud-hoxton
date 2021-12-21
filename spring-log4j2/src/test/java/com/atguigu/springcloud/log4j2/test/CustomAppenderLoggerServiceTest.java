package com.atguigu.springcloud.log4j2.test;

import com.atguigu.springcloud.Log4j2Main8080;
import com.atguigu.springcloud.logs.CustomAppenderLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Log4j2Main8080.class)
@Slf4j
public class CustomAppenderLoggerServiceTest {

    @Resource
    private CustomAppenderLoggerService customAppenderLoggerService;

    @Test
    public void printLogTest() {
        customAppenderLoggerService.printLog("你是一只大笨猪");
        customAppenderLoggerService.printLog("你是一只小笨鸭");
    }

}
