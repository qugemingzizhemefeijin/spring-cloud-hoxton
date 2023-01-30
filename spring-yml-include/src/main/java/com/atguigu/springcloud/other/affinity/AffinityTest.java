package com.atguigu.springcloud.other.affinity;

import net.openhft.affinity.AffinityLock;

// 将程序绑定到指定CPU核运行（俗称绑核）
public class AffinityTest {

    public static void main(String[] args) {
        try (AffinityLock affinityLock = AffinityLock.acquireLock(5)) {
            // do some work while locked to a CPU.
            while(true) {}
        }
    }

}
