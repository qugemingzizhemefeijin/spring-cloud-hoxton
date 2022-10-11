package com.atguigu.springcloud.other;

import org.springframework.core.Ordered;

public interface AopOrders {

    int HIGH = Ordered.HIGHEST_PRECEDENCE;

    int DISTRIBUTION_LOCK_ORDER = 1;

    int TX_ORDERED = 2;

    int LOW = Ordered.LOWEST_PRECEDENCE;

}
