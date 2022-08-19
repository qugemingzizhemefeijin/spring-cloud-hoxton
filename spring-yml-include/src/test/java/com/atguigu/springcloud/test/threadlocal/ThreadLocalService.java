package com.atguigu.springcloud.test.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadLocalService {

    /**
     * 跨线程传递ThreadLocal变量。（这里有个问题，就是只有新建的线程才能获取父线程的ThreadLocal，否则不会因为父线程变动而造成变动）
     */
    @Test
    public void inheritableThreadLocalTest() {
        InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set(6);
        log.info("父线程获取数据：" + threadLocal.get());

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        threadLocal.set(7);
        executorService.submit(() -> {
            log.info("第一次从线程池中获取数据：" + threadLocal.get());
        });

        threadLocal.set(8);
        executorService.submit(() -> {
            log.info("第二次从线程池中获取数据：" + threadLocal.get());
        });
    }

    /**
     * 淘宝支持跨线程变动ThreadLocal
     * <p>
     * 使用 Future.get() 时，尽量使用带超时时间的，因为它是阻塞的。
     * <p>
     * 如果线程池拒绝策略设置不合理，就容易有坑。
     * 我们把拒绝策略设置为DiscardPolicy或DiscardOldestPolicy并且在被拒绝的任务，Future对象调用get()方法,那么调用线程会一直被阻塞。
     *
     * @see com.alibaba.ttl.TtlRunnable
     * @see com.alibaba.ttl.threadpool.TtlExecutors#getTtlExecutorService
     */
    @Test
    public void transmittableThreadLocalTest() {
        // 代码中除了使用TransmittableThreadLocal类之外，还使用了TtlExecutors.getTtlExecutorService方法，去创建ExecutorService对象。
        // 这是非常重要的地方，如果没有这一步，TransmittableThreadLocal在线程池中共享数据将不会起作用。
        // 创建ExecutorService对象，底层的submit方法会TtlRunnable或TtlCallable对象。
        // 以TtlRunnable类为例，它实现了Runnable接口，同时还实现了它的run方法。
        /*
            final Object captured = capturedRef.get();
            if (captured == null || releaseTtlValueReferenceAfterRun && !capturedRef.compareAndSet(captured, null)) {
                throw new IllegalStateException("TTL value reference is released after run!");
            }

            final Object backup = replay(captured);
            try {
                runnable.run();
            } finally {
                restore(backup);
            }
         */
        TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
        threadLocal.set(6);
        log.info("父线程获取数据：" + threadLocal.get());

        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));

        threadLocal.set(7);
        ttlExecutorService.submit(() -> {
            log.info("第一次从线程池中获取数据：" + threadLocal.get());
        });

        threadLocal.set(8);
        ttlExecutorService.submit(() -> {
            log.info("第二次从线程池中获取数据：" + threadLocal.get());
        });

    }

}
