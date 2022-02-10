package com.atguigu.springcloud;

import com.atguigu.springcloud.easyexcel.domain.CustomAttributeVO;
import com.atguigu.springcloud.easyexcel.domain.Order;
import com.atguigu.springcloud.service.OrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MouztBizlogTest {

    @Autowired
    private OrderService orderService;

    private Order order = null;

    @Before
    public void init() {
        order = new Order();
        order.setOrderNo("123");
        order.setPurchaseName("小橙子");
        order.setProductName("红烧肉");
    }

    @Test
    public void createSuccessOrderTest() {
        orderService.createSuccessOrder(order);
    }

    @Test
    public void createFailureOrderTest() {
        orderService.createFailureOrder(order);
    }

    @Test
    public void createAddCategoryOrderTest() {
        orderService.createAddCategoryOrder(order);
    }

    @Test
    public void createOrderAndDetailTest() {
        orderService.createOrderAndDetail(order);
    }

    @Test
    public void createOrderAndOperatorTest() {
        orderService.createOrderAndOperator(order, "完美时空");
    }

    @Test
    public void updateNoCustomFunctionTest() {
        orderService.updateNoCustomFunction(123L, order);
    }

    @Test
    public void updateCustomFunctionTest() {
        orderService.updateCustomFunction(123L, order);
    }

    @Test
    public void disableAttributeTest() {
        CustomAttributeVO vo = orderService.disableAttribute(9527L, 9366L, false);
        log.info(vo.toString());
    }

    @Test
    public void createContextVariableOrderTest() {
        orderService.createContextVariableOrder(order);
    }

    @Test
    public void updateRequirementDocLinkTest() {
        List<String> docLinks = Lists.newArrayList("0", "2", "3");
        orderService.updateRequirementDocLink("123", 9527L, docLinks);
    }

    @Test
    public void userOperatorOrderTest() {
        orderService.userOperatorOrder("1", order);
    }

    @Test
    public void changeOrderValueTest() {
        orderService.changeOrderValue(order);
    }

}
