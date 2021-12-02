package com.atguigu.springcloud.service;

import com.atguigu.drools.pojo.Person;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.definitions.rule.impl.RuleImpl;

@Slf4j
public class PersonRuleAction {

    // 目前只实现记录日志功能
    public static void doParse(Person person, RuleImpl rule) {
        log.debug("{} is matched Rule[{}]!", person, rule.getName());
    }

}
