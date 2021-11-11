package com.atguigu.springcloud.test;

import com.atguigu.springcloud.DroolsMain8080;
import com.atguigu.springcloud.service.KieService;
import com.atguigu.springcloud.vo.ReqAndResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DroolsMain8080.class)
@Slf4j
public class KieServiceTest {

    @Resource
    private KieService kieService;

    @Test
    public void executeTest(){
        kieService.loadRules("1001");

        ReqAndResult res = new ReqAndResult();
        res.setRuleCode("hello");
        res.setReq("你是一只猪");

        kieService.execute(res);

        System.out.println(res.getResult());
    }

}
