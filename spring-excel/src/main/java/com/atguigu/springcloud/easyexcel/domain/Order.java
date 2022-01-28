package com.atguigu.springcloud.easyexcel.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Order {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 订单编码
     */
    private String orderSn;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 收货时间
     */
    private String receiverAddress;

    /**
     * 收货人
     */
    private Member member;

    /**
     * 商品列表
     */
    private List<Product> productList;

}
