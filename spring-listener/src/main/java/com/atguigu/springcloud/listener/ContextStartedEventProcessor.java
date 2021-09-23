package com.atguigu.springcloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * ContextStartedEvent
 *
 * 当使用 ConfigurableApplicationContext （ApplicationContext子接口）接口中的 start() 方法启动 ApplicationContext 时，该事件被发布。
 * 你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。
 */
@Slf4j
@Component
public class ContextStartedEventProcessor implements ApplicationListener<ContextStartedEvent> {

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        log.info("ApplicationContext start ... event = {}", event);
    }

}
