springboot中使用drools规则引擎，[Drools中文网](http://www.drools.org.cn/)

### 1.KIESession 与 StatelessKIESession

#### 一、Session

分为两类
- Stateful 有状态的会话
- Stateless 无状态的会话

#### 二、KIESession

会在多次与规则引擎进行交互中，维护会话的状态。

**1：定义KieSession**

在`kmodule.xml`文件中定义`type`为`stateful`的`session`：
```
<ksession name="stateful_session" type="stateful"></ksession>
```

Tip：`stateful`是`type`属性的默认值。

**2：获取 KIESession 实例**

```
KieSession statefulSession = kieContainer.newKieSession("stateful_session");
```

接下来，可以在`KIESession`执行一些操作。

最后，如果需要清理`KIESession``维护的状态，调用`dispose()`方法。

#### 三、StatelessKIESession

`StatelessKIESession`隔离了每次与规则引擎的交互，不会维护会话的状态。

1：使用场景

- 数据校验
- 运算
- 数据过滤
- 消息路由
- 任何能被描述成函数或公式的规则

2：定义StatelessKIESession

在`kmodule.xml`文件中定义`type`为`stateless`的`session`：
```
<ksession name="stateless_session" type="stateless"></ksession>
```

3：获取 StatelessKIESession 实例
```
StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession("stateless_session");
```

4：拓展

`KieCommands`通过`KieServices`获取`command`工厂类`KieCommands`：
```
KieCommands commandFactory = kieServices.getCommands();
```

可以使用工程类`KieCommands`调用`newXXXCommand`开头的方法创建`command`实例。会话执行`command`：
```
statelessKieSession.execute(command);
```

#### 四、规则then

1、update(Fact对象)：它会告诉引擎一个对象已经改变了（一个绑定到LHS部分上的引用、即$p:Person中的$p），修改成功后在工作内存中会发生变化，可能会导致规则再次激活。

2、insert(new Object)：将一个新的Fact对象放入工作内存中、它不仅可以操作引用的Fact对象，还可以操作declare声明的Fact对象。

3、insertLogical(new Object)：与inser类似，但是当没有更多的对象来支持当前触发规则的LHS部分时，该Fact对象将自动删除。使用得并不多。

4、delete(handle)：从工作内存中删除一个Fact对象，其语法与update相似，都是通过引用LHS部分上绑定的值。

#### 五、kmodule属性说明

KieBase属性说明：

| 属性名称 | 是否必填 | 值 | 说明 |
| --- | --- | --- | --- |
| name | 是 | string | 用于从KieContainer检索该KieBase的名称。既是规则库的名称，又是唯一的强制属性。最好规范命名，见名知意。|
| packages | 否 | string | 默认情况下，加载资源文件夹中所有Drools相关文件，此属性目的是将当前路径下的规则文件在KieBase中编译。仅限于指定的软件包路径下的所有文件。可以是多个，以逗号分割。|
| includes | 否 | string | 就是集成另一个KieBase的资源 |
| default | 否 | boolean | 是否是默认的，每个Module中最多只能有一个，默认为false |
| equalsBehavior | 否 | Identity/equality | 将新Fact对象插入工作内存中时，定义了Drools的操作。使用了身份属性，那么它会创建一个新的FactHandle，除非相同的对象不存在于工作内存中，而只有新插入的对象与已存在的对象不相等时才会相同 |
| eventProcessingMode | 否 | Cloud/Stream | 当以云模式编译时，KieBase将事件视为正常，而在云模式下可以对其进行时间推理。多在Workbench中使用 |
| declarativeAgenda | 否 | Disabled/enable | 定义声明议程是否启用 |

KieSession属性说明：

| 属性名称 | 是否必填 | 值 | 说明 |
| --- | --- | --- | --- |
| name | 是 | string | 这个是KieSession的名称。用于从KieContainer中获取KieSession。这是唯一的强制属性，用于指定操作规则的会话 |
| type | 否 | stateful/stateless | 指当前KieSession的类型是有状态的还是无状态的（默认不指定为有状态的） |
| default | 否 | true/false | 定义此KieSession是否为该模块的默认值，如果该值为true，则可以从KieContainer中创建，而不会传递任何名称在每个模块中，每个类型最多可以有一个默认的KieSession，且是有状态的 |
| clockType | 否 | realtime/seudo | 定义事件时间戳是由系统时钟还是由应用程序控制的seudo时钟确定。该时钟对于单元测试时间规则特别有用 |
| beliefSystem | 否 | simple/tms/defeasible | 定义KieSession使用的belief System的类型 |

#### 六、insert、update、modify、delete函数

insert：函数`insert`的作用与外面在Java类调用KieSession对象的insert方法的作用相同，都是用来将一个Fact对象插入到当前的`Working Memory`当中。

update/modify更新：`update/modify`用来实现对当前`Working Memory`当中的Fact进行更新，update宏函数的作用与StatefulSession对象的update方法的作用基本相同，用来告诉当前`Working Memory`该Fact对象已经发生了变化。

delete/retract删除：和KieSession的`retract/delete`方法一样，宏函数`retract/delete`也是用来将`Working Memory`当中某个Fact对象从`Working Memory`当中删除。