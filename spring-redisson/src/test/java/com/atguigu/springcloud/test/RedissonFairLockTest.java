package com.atguigu.springcloud.test;

import com.atguigu.springcloud.RedissonFairLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedissonFairLockTest {

    @Autowired
    private RedissonFairLock redissonFairLock;

    @Test
    public void easyLockTest() throws InterruptedException {
        int count = 10;
        CountDownLatch latch = new CountDownLatch(count);
        //模拟多个10个客户端
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(new LockRunnable(latch));
            thread.start();
        }

        latch.await();
        System.out.println("over");
    }

    private class LockRunnable implements Runnable {

        private final CountDownLatch latch;

        public LockRunnable(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            redissonFairLock.addLock("demo");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                redissonFairLock.releaseLock("demo");
                latch.countDown();
            }
        }
    }

}
