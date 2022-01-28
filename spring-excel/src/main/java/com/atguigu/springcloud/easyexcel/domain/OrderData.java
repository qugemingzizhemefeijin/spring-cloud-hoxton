package com.atguigu.springcloud.easyexcel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.atguigu.springcloud.easyexcel.CustomMerge;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单导出
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderData implements Serializable {

    private static final long serialVersionUID = -3311661352523412762L;

    @ExcelProperty(value = "订单ID")
    @ColumnWidth(10)
    @CustomMerge(needMerge = true, isPk = true)
    private String id;

    @ExcelProperty(value = "订单编码")
    @ColumnWidth(20)
    @CustomMerge(needMerge = true)
    private String orderSn;

    @ExcelProperty(value = "创建时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    @CustomMerge(needMerge = true)
    private Date createTime;

    @ExcelProperty(value = "收货地址")
    @CustomMerge(needMerge = true)
    @ColumnWidth(20)
    private String receiverAddress;

    @ExcelProperty(value = {"商品信息", "商品编码"})
    @ColumnWidth(20)
    private String productSn;

    @ExcelProperty(value = {"商品信息", "商品名称"})
    @ColumnWidth(20)
    private String name;

    @ExcelProperty(value = {"商品信息", "商品标题"})
    @ColumnWidth(30)
    private String subTitle;

    @ExcelProperty(value = {"商品信息", "品牌名称"})
    @ColumnWidth(20)
    private String brandName;

    @ExcelProperty(value = {"商品信息", "商品价格"})
    @ColumnWidth(20)
    private BigDecimal price;

    @ExcelProperty(value = {"商品信息", "商品数量"})
    @ColumnWidth(20)
    private Integer count;

}
