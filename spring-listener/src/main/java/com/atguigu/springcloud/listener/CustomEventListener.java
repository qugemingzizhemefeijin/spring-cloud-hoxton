package com.atguigu.springcloud.listener;

import com.atguigu.springcloud.event.CustomEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomEventListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof CustomEvent) {
            CustomEvent ce = (CustomEvent) event;
            log.info("CustomEvent msg = {}", ce.getMsg());
        } else {
            log.info("容器本身事件：" + event);
        }
    }

}
