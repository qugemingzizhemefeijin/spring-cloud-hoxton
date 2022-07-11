### spring中状态机的使用。

#### Spring Statemachine状态机

`Spring Statemachine`是`Spring`官方提供的一个框架，供应用程序开发人员在Spring应用程序中使用状态机。支持状态的嵌套（`substate`），状态的并行（`parallel`，`fork`，`join`）、子状态机等等。

官网地址：[https://projects.spring.io/spring-statemachine/](https://projects.spring.io/spring-statemachine/)

`Spring Statemachine`的设计是有状态的，每个`statemachine`实例内部保存了当前状态机上下文相关的属性（比如当前状态、上一次状态等），因此是*线程不安全的*，所以代码要么需要使用锁同步，要么需要用`ThreadLocal`，非常的痛苦和难用。因此，在实际场景中，基本上都是利用工厂创建状态机。

说到底`Spring StateMachine`上手难度非常大，如果没有用来做重型状态机的需求，十分不推荐普通的小项目进行接入。

案例：

https://blog.csdn.net/qq_22076345/article/details/109266901

https://www.jianshu.com/p/9ee887e045dd

