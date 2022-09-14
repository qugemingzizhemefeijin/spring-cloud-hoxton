package com.atguigu.springcloud.watchservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// WatchService是监视服务接口, 在不同系统上有不同的实现类。实现了Watchable接口的对象方可注册监视服务，java.nio.file.Path实现了此接口;
// 缺点：
// 无法监控子目录下文件的变化
// 大文件夹下，超慢
public class CustomJnotifyAdapter {

    private String rootDir;

    private FileWatchServiceTask task;

    private final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5);

    public CustomJnotifyAdapter(String rootDir) {
        this.rootDir = rootDir;
    }

    public void beginWatch() {
        task = new FileWatchServiceTask(rootDir);
        cachedThreadPool.execute(task);
    }

    public static void main(String[] args) {
        String dir = "e:/eb";
        new CustomJnotifyAdapter(dir).beginWatch();
    }

}
