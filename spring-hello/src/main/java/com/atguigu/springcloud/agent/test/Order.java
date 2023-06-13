package com.atguigu.springcloud.agent.test;

import lombok.Data;

@Data
public class Order {

    private String orderId;

    private String goodsName;

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                '}';
    }
}
