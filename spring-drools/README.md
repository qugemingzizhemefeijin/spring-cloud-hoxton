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
