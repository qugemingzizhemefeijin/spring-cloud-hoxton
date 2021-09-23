package com.atguigu.springcloud.listener;

import com.atguigu.springcloud.event.SmartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmartEventListener implements SmartApplicationListener {

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        SmartEvent smart = (SmartEvent)event;

        log.info("smart event, msg = {}", smart.getMsg());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == SmartEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

}
