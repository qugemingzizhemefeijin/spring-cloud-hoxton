package com.atguigu.springcloud.easyexcel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private Long orderId;
    private String orderNo;
    private String purchaseName;
    private String productName;
    private Date createTime;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", purchaseName='" + purchaseName + '\'' +
                ", productName='" + productName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
