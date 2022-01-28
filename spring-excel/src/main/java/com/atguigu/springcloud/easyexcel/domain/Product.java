package com.atguigu.springcloud.easyexcel.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品编码
     */
    private String productSn;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品标题
     */
    private String subTitle;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private Integer count;

}
