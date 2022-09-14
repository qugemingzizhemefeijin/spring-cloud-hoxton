package com.atguigu.springcloud.jnotiy;

public class JnotifyTest {

    public static void main(String[] args) throws Exception {
        String dir = "e:/eb";
        new CustomJnotifyAdapter(dir).beginWatch();
    }

}
