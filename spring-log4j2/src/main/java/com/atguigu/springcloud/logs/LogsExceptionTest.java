package com.atguigu.springcloud.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class LogsExceptionTest implements InitializingBean {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            List<String> testList = new ArrayList<>();
            while(true) {
                log.info("Exception Log4j2 {}", counter.incrementAndGet());
                try {
                    TimeUnit.SECONDS.sleep(10);
                    testList.add(new String(new byte[1024*1024*1024]));
                    log.info("Exception Log4j2 RunTimeException {}", counter.incrementAndGet());
                    log.info(testList.get(0));
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                    testList.clear();
                }
            }
        }).start();
    }

}
