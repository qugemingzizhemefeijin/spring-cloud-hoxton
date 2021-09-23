package com.atguigu.springcloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * ContextRefreshedEvent
 *
 * ApplicationContext 被初始化或刷新时，该事件被发布。
 * 这也可以在 ConfigurableApplicationContext接口中使用 refresh() 方法来发生。
 * 此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用。
 */
@Slf4j
@Component
public class ContextRefreshedEventProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("all bean load success, event = {}", event);
    }

}
