package com.atguigu.springcloud.easyexcel.domain;

import com.fastobject.diff.DiffLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable, Diffable<Order> {

    @DiffLog(name = "订单ID")
    private Long orderId;
    @DiffLog(name = "订单号")
    private String orderNo;
    @DiffLog(name = "购买人")
    private String purchaseName;
    @DiffLog(name = "商品名称")
    private String productName;
    @DiffLog(name = "开始时间", dateFormat = "yyyy-dd-MM hh:mm:ss")
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

    @Override
    public DiffResult diff(Order obj) {
        return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("orderId", this.orderId, obj.orderId)
                .append("orderNo", this.orderNo, obj.orderNo)
                .append("purchaseName", this.purchaseName, obj.purchaseName)
                .append("productName", this.productName, obj.productName)
                .append("createTime", this.createTime, obj.createTime)
                .build();
    }
}
