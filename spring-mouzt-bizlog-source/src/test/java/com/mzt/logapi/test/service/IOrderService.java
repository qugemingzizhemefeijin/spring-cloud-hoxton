package com.mzt.logapi.test.service;

import com.mzt.logapi.test.bean.Order;

public interface IOrderService {

    boolean createOrder(Order order);

    boolean update(Long orderId, Order order);

    boolean testCondition(Long orderId, Order order, String condition);

    boolean testContextCallContext(Long orderId, Order order);

}
