package com.atguigu.springcloud.test.async;

import com.atguigu.springcloud.AsyncTask;
import com.atguigu.springcloud.YmlIncludeMain8080;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YmlIncludeMain8080.class)
@Slf4j
public class AsyncTest {

    @Autowired
    private AsyncTask asyncTask;

    @Test
    public void asyncTaskTest() {
        for (int i = 0; i < 100; i++) {
            asyncTask.doTask1(i);
        }
        log.info("All tasks finished.");
    }

    @Test
    public void asyncTaskNativeTest() {
        for (int i = 0; i < 100; i++) {
            asyncTask.doTask2(i);
        }
        log.info("All tasks finished.");
    }

}
