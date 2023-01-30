package com.atguigu.springcloud;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class HotCode {

    public static void main(String[] args) {
        allocMethod();
        cpuMethod();
        randomMethod();
    }

    private static void allocMethod() {
        // 每次分配10W大小的数组
        int[] a = new int[100000];
    }

    private static void cpuMethod() {
        ArrayList<String> list = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString().replace("-", "");
        list.add(str);
    }

    private static void randomMethod() {
        Random random = new Random();
        int anInt = random.nextInt();
    }

}
