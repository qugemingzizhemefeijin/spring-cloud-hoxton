package com.atguigu.springcloud.test.internal;

//import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.util.HashMap;

public class InternalTest {

    public static void main(String[] args) {
        // 对象头分布
        Object obj = new Object();
        String s = ClassLayout.parseInstance(obj).toPrintable();
        System.out.println(s);

        // 判断对象大小
        //long cc = ObjectSizeCalculator.getObjectSize(new HashMap<>());
        //System.out.println(cc);

        //
        s = GraphLayout.parseInstance(obj).toPrintable();
        System.out.println(s);

        // 数组
        int[] b = new int[10];
        s = ClassLayout.parseInstance(b).toPrintable();
        System.out.println(s);
    }

}
