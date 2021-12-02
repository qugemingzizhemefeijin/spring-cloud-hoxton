package com.atguigu.springcloud.test;

import com.atguigu.drools.pojo.Person;
import com.atguigu.springcloud.DroolsMain8080;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DroolsMain8080.class)
@Slf4j
public class KieDroolsTest {

    @Resource
    private KieSession ksession;

    @Test
    public void test1findTargetPerson() {
        System.out.println(ksession);
        Person zs = new Person();
        zs.setName("张三");
        ksession.insert(zs);

        Person other = new Person();
        other.setName("other1");
        ksession.insert(other);

        int rules = ksession.fireAllRules();
        // 按照规则1只有bob能够匹配上
        log.info("rules = {}", rules);
    }

}
