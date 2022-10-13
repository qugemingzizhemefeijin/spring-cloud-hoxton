package com.atguigu.springcloud.test.stopwatch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * Spring StopWatch 使用
 */
@Slf4j
public class StopWatchTest {

    @Test
    public void stopWatchTest() throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("start query");
        TimeUnit.SECONDS.sleep(10);
        sw.stop();
        sw.start("start handle");
        TimeUnit.SECONDS.sleep(5);
        sw.stop();

        log.info("{}", sw.prettyPrint());

        System.out.println(System.identityHashCode(sw));
        System.out.println(sw.hashCode());
    }

}
