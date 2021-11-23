package com.atguigu.springcloud.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class LogsTest implements InitializingBean {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void afterPropertiesSet() throws Exception {
        /*new Thread(() -> {
            while(true) {
                log.info("Hello Log4j2 {}", counter.incrementAndGet());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }).start();*/
    }

}
