package com.atguigu.springcloud.functions;

import com.atguigu.springcloud.easyexcel.domain.Order;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OrderParseFunction implements IParseFunction {

    //@Resource
    //@Lazy //为了避免类加载顺序的问题 最好为Lazy，没有问题也可以不加
    //private OrderQueryService orderQueryService;

    @Override
    public String functionName() {
        //  函数名称为 ORDER
        return "ORDER";
    }

    @Override
    //这里的 value 可以吧 Order 的JSON对象的传递过来，然后反解析拼接一个定制的操作日志内容
    public String apply(String value) {
        if(StringUtils.isEmpty(value)){
            return value;
        }
        //Order order = orderQueryService.queryOrder(Long.parseLong(value));
        Order order = new Order();
        order.setProductName("小马宝贝");
        order.setPurchaseName("小橙子");
        order.setOrderNo("9527");

        //把订单产品名称加上便于理解，加上 ID 便于查问题
        return order.getProductName().concat("(").concat(value).concat(")");
    }

}
