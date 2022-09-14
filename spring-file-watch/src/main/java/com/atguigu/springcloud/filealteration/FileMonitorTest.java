package com.atguigu.springcloud.filealteration;

import com.atguigu.springcloud.watchservice.CustomJnotifyAdapter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.util.concurrent.TimeUnit;

// 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
// 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
// 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
// 缺点：
// 内部实现是遍历的方式，小文件夹的效率还好； 比如用测试60G的目录测试，就很慢了
public class FileMonitorTest {

    public static void main(String[] args) throws Exception {
        String dir = "e:/eb";
        long intervalTime = TimeUnit.SECONDS.toMillis(5);

        FileAlterationObserver observer2 = new FileAlterationObserver(dir,
                FileFilterUtils.and(
                        FileFilterUtils.fileFileFilter(),
                        FileFilterUtils.suffixFileFilter(".txt")
                )
        );

        FileAlterationObserver observer = new FileAlterationObserver(dir);

        //Set file change listener
        observer.addListener(new FileListener());
        //Create file change monitor
        FileAlterationMonitor monitor = new FileAlterationMonitor(intervalTime, observer);
        //start monitor
        monitor.start();
    }

}
