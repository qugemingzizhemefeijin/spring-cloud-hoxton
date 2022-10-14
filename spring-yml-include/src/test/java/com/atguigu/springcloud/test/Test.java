package com.atguigu.springcloud.test;

import org.assertj.core.util.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;

// -server -Xms30m -Xmx30m -Xmn20M -XX:SurvivorRatio=4 -Xss512K -XX:PretenureSizeThreshold=921600 -XX:+ExplicitGCInvokesConcurrent -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:StringTableSize=299993 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=95 -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSParallelInitialMarkEnabled -XX:+CMSParallelRemarkEnabled -XX:CMSFullGCsBeforeCompaction=3 -XX:+UseCMSCompactAtFullCollection -XX:+CMSClassUnloadingEnabled -XX:+ScavengeBeforeFullGC -XX:+ParallelRefProcEnabled -XX:-OmitStackTraceInFastThrow -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -XX:+PrintGCDateStamps -XX:+PrintGCCause
public class Test {

    private static int _1M = 1024 * 1024;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start");
        // 老年代10M
        List<byte[]> list = Lists.newArrayList();

        list.add(new byte[_1M * 1]);
        byte[] b = new byte[_1M * 1];
        list.add(new byte[_1M * 1]);
        byte[] c = new byte[_1M * 1];
        list.add(new byte[_1M * 4]);
        TimeUnit.SECONDS.sleep(2);
        // 总共9M
        b = null;
        c = null;
        // 第一次GC Foreground GC，是由于内存分配的时候，由于内存不足，导致分配失败的GC（这个GC STW，只回收老年代），不会触发内存碎片整理。
        // 第二次GC Serial GC，是由于内存碎片原因，明明内存充足，但是却无法申请足够的内存，导致发生Serial GC（这个GC SWT，全推回收），并触发内存整理。
        // 空间不足触发CMS，b,c被回收，实际空间3M，但是2M申请不下来，空间碎片了
        list.add(new byte[_1M * 2]);

        TimeUnit.SECONDS.sleep(20);
    }

}
