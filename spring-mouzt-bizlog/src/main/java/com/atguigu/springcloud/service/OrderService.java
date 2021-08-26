package com.atguigu.springcloud.service;

import com.atguigu.springcloud.constant.LogRecordType;
import com.atguigu.springcloud.domain.CustomAttributeVO;
import com.atguigu.springcloud.domain.Order;
import com.google.common.collect.Lists;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecordAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    /*
     * 普通的记录日志
     * pefix：是拼接在 bizNo 上作为 log 的一个标识。避免 bizNo 都为整数 ID 的时候和其他的业务中的 ID 重复。比如订单 ID、用户 ID 等
     * bizNo：就是业务的 ID，比如订单 ID，我们查询的时候可以根据 bizNo 查询和它相关的操作日志
     * success：方法调用成功后把 success 记录在日志的内容中
     * SpEL 表达式：其中用双大括号包围起来的（例如：{{#order.purchaseName}}）#order.purchaseName 是 SpEL 表达式。Spring 中支持的它都支持的。
     * 比如调用静态方法，三目表达式。SpEL 可以使用方法中的任何参数
     */
    @LogRecordAnnotation(success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createSuccessOrder(Order order) {
        log.info("【创建订单成功】orderNo={}", order.getOrderNo());
        // db insert order
        return true;
    }

    /*
     * 期望记录失败的日志, 如果抛出异常则记录 fail 的日志，没有抛出记录 success 的日志
     */
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createFailureOrder(Order order) {
        log.info("【创建订单失败】orderNo={}", order.getOrderNo());
        // db insert order
        int i = 0;
        int j = 10 / i;
        System.out.println(j);
        return true;
    }

    /*
     * 比如一个订单的操作日志，有些操作日志是用户自己操作的，有些操作是系统运营人员做了修改产生的操作日志，我们系统不希望把运营的操作日志暴露给用户看到，
     * 但是运营期望可以看到用户的日志以及运营自己操作的日志，这些操作日志的 bizNo 都是订单号，所以为了扩展添加了类型字段, 主要是为了对日志做分类，查询方便，支持更多的业务
     */
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            category = "MANAGER",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createAddCategoryOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        return true;
    }

    /*
     * 如果一个操作修改了很多字段，但是 success 的日志模版里面防止过长不能把修改详情全部展示出来，这时候需要把修改的详情保存到 detail 字段，
     * detail 是一个 String ，需要自己序列化。这里的 #order.toString() 是调用了 Order 的 toString() 方法。
     * 如果保存 JSON，自己重写一下 Order 的 toString() 方法就可以。
     */
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            category = "MANAGER_VIEW",
            detail = "{{#order.toString()}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrderAndDetail(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        return true;
    }

    /*
     * 如何指定操作日志的操作人是什么？
     * 第一种：手工在 LogRecord 的注解上指定。这种需要方法参数上有 operator [这种不灵哦]
     *
     * 第二种：通过默认实现类来自动的获取操作人，由于在大部分 web 应用中当前的用户都是保存在一个线程上下文中的，所以每个注解都加一个 operator
     * 获取操作人显得有些重复劳动，所以提供了一个扩展接口来获取操作人
     *
     * 框架提供了一个扩展接口，使用框架的业务可以 implements 这个接口自己实现获取当前用户的逻辑，对于使用 Springboot 的只需要实现 IOperatorGetService 接口，
     * 然后把这个 Service 作为一个单例放到 Spring 的上下文中。使用 Spring Mvc 的就需要自己手工装配这些 bean 了。
     *
     */
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            category = "MANAGER_VIEW",
            detail = "{{#order.toString()}}",
            operator = "{{#currentUser}}",
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrderAndOperator(Order order, String currentUser) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        return true;
    }

    /*
     * 对于更新等方法，方法的参数上大部分都是订单 ID、或者产品 ID 等，
     * 比如下面的例子：日志记录的 success 内容是：“更新了订单 {{#orderId}}, 更新内容为…”，这种对于运营或者产品来说难以理解，所以引入了自定义函数的功能。
     *
     * 使用方法是在原来的变量的两个大括号之间加一个函数名称 例如 “{ORDER{#orderId}}” 其中 ORDER 是一个函数名称。只有一个函数名称是不够的, 需要添加这个函数的定义和实现。
     *
     * 下面例子自定义的函数需要实现框架里面的 IParseFunction 的接口，需要实现两个方法：
     * functionName() 方法就返回注解上面的函数名；
     *
     * apply() 函数参数是 "{ORDER{#orderId}}" 中 SpEL 解析的 #orderId 的值，这里是一个数字 1223110，接下来只需要在实现的类中把 ID 转换为可读懂的字符串就可以了，
     * 一般为了方便排查问题需要把名称和 ID 都展示出来，例如："订单名称（ID）" 的形式。
     *
     */

    // 没有使用自定义函数
    @LogRecordAnnotation(success = "更新了订单{{#orderId}},更新内容为....",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}")
    public boolean updateNoCustomFunction(Long orderId, Order order) {
        return false;
    }

    //使用了自定义函数，主要是在 {{#orderId}} 的大括号中间加了 functionName
    @LogRecordAnnotation(success = "更新了订单{ORDER{#orderId}},更新内容为...",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}")
    public boolean updateCustomFunction(Long orderId, Order order) {
        return false;
    }

    //日志文案调整 使用 SpEL 三目表达式
    @LogRecordAnnotation(prefix = LogRecordType.CUSTOM_ATTRIBUTE, bizNo = "{{#businessLineId}}",
            success = "{{#disable ? '停用' : '启用'}}了自定义属性{ATTRIBUTE{#attributeId}}")
    public CustomAttributeVO disableAttribute(Long businessLineId, Long attributeId, boolean disable) {
        return new CustomAttributeVO(businessLineId, attributeId);
    }

    // 日志文案调整 模版中使用方法参数之外的变量
    /*
     * 可以在方法中通过 LogRecordContext.putVariable(variableName, Object) 的方法添加变量，第一个对象为变量名称，后面为变量的对象，
     * 然后我们就可以使用 SpEL 使用这个变量了，例如：例子中的 {{#innerOrder.productName}} 是在方法中设置的变量
     *
     */
    @LogRecordAnnotation(
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createContextVariableOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }

    // 函数中使用 LogRecordContext 的变量
    /*
     * 使用 LogRecordContext.putVariable(variableName, Object) 添加的变量除了可以在注解的 SpEL 表达式上使用，还可以在自定义函数中使用, 这种方式比较复杂，
     * 下面例子中示意了列表的变化，比如从 [A,B,C] 改到 [B,D] 那么日志显示：「删除了 A，增加了 D」
     */
    @LogRecordAnnotation(success = "{DIFF_LIST{'文档地址'}}", bizNo = "{{#id}}", prefix = LogRecordType.REQUIREMENT)
    public void updateRequirementDocLink(String currentMisId, Long id, List<String> docLinks) {
        List<String> oldList = Lists.newArrayList("1", "2", "3");
        LogRecordContext.putVariable("oldList", oldList);
        LogRecordContext.putVariable("newList", docLinks);
    }

    //使用了自定义函数，主要是在 {{#orderId}} 的大括号中间加了 functionName
    @LogRecordAnnotation(success = "{USER{#userId}}操作了订单{{#order.orderNo}}...",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}")
    public boolean userOperatorOrder(String userId, Order order) {
        return false;
    }

}
