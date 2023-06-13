package com.atguigu.springcloud.agent.test;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println("启动");

        Client test = new Client();
        new Thread(() -> {
            int i = 0;
            while(true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Order order = test.getOrderDetail("AT100200302003002");
                System.out.println(order);
                System.out.println(test.getOrderCount(i++));
            }
        }).start();

        System.in.read();
    }

}
