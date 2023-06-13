package com.atguigu.springcloud.agent.test;

public class Client {

    public Order getOrderDetail(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setGoodsName("我的娜塔莎");
        return order;
    }

    public int getOrderCount(int c) {
        System.out.println("I get C = " + c);
        return 0;
    }

}
