package com.atguigu.springcloud;

import com.atguigu.springcloud.easyexcel.domain.Order;
import com.fastobject.diff.AbstractObjectDiff;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;

public class Test {

    public static void main(String[] args) throws Exception {
        Order order1 = getOrder1();
        Order order2 = getOrder2();
        annotationDiffUtil(order1, order2);
        diffObject(order1, order2);
    }

    private static void diffObject(Order order1, Order order2) {
        DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(order1, order2);
        diffNode.visit((diffNode1, visit) -> System.out.println(diffNode1.getPath() + " => " + diffNode1.getState() + "###" +
                diffNode1.canonicalGet(order1) + "@@@@" + diffNode1.canonicalGet(order2)));
    }

    private static void annotationDiffUtil(Order order1, Order order2) throws Exception {
        String chineseDiffStr = AbstractObjectDiff.genChineseDiffStr(order1, order2);
        System.out.println(chineseDiffStr);
    }

    private static Order getOrder2() {
        Order order = new Order();
        order.setOrderNo("111111");
        order.setPurchaseName("小橙子");
        order.setProductName("红烧肉333");
        return order;
    }

    private static Order getOrder1() {
        Order order = new Order();
        order.setOrderNo("2222222");
        order.setPurchaseName("小橙子");
        order.setProductName("红烧肉");

        return order;
    }

}
