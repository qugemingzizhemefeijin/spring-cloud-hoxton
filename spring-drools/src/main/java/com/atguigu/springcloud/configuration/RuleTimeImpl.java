package com.atguigu.springcloud.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

@Slf4j
public class RuleTimeImpl implements RuleRuntimeEventListener {

    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        log.info("输出Insert监听方法");
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        String name = event.getOldObject().toString();
        log.info("输出Update监听方法{}", name);
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        log.info("输出Delete监听方法");
    }

}
