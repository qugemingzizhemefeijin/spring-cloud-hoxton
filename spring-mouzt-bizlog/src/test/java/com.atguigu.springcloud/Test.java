package com.atguigu.springcloud;

import com.atguigu.springcloud.easyexcel.domain.Order;
import com.fastobject.diff.AbstractObjectDiff;
import com.fastobject.diff.DiffUtils;
import com.fastobject.diff.DiffWapper;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;

import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        Order order1 = getOrder1();
        Order order2 = getOrder2();
        annotationDiffUtil(order1, order2);

        System.out.println("==============1===================");

        diffObject(order1, order2);

        System.out.println("==============2===================");

        apacheDiff(order1, order2);

        System.out.println("==============3===================");

        javersDiff(order1, order2);

        System.out.println("==============4===================");
    }

    // 这个最后维护时间在2018年
    private static void diffObject(Order order1, Order order2) {
        DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(order1, order2);
        diffNode.visit((diffNode1, visit) -> System.out.println(diffNode1.getPath() + " => " + diffNode1.getState() + "###" +
                diffNode1.canonicalGet(order1) + "@@@@" + diffNode1.canonicalGet(order2)));
    }

    // 这个一直还在维护中
    private static void annotationDiffUtil(Order order1, Order order2) throws Exception {
        // String chineseDiffStr = AbstractObjectDiff.genChineseDiffStr(order1, order2);

        List<DiffWapper> chineseDiffStr = AbstractObjectDiff.generateDiff(order1, order2);

        System.out.println(DiffUtils.genDiffStr(chineseDiffStr));
    }

    private static void apacheDiff(Order order1, Order order2) {
        DiffResult result = order1.diff(order2);

        List<Diff<?>> list = result.getDiffs();
        for (Diff<?> diff : list) {
            System.out.println(diff.getFieldName() + ":" + diff.getValue() + "--" + diff.getLeft() + "--" + diff.getRight() + "--" + diff.getKey() + ":" + diff.getType());
        }
        System.out.println();
    }

    private static void javersDiff(Order order1, Order order2) {
        Javers javers = JaversBuilder.javers().build();
        org.javers.core.diff.Diff diff = javers.compare(order1, order2);

        if (diff.hasChanges()) {
            Changes changes = diff.getChanges();
            changes.forEach(System.out::println);
        }

        System.out.println(diff.prettyPrint());
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
