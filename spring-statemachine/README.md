### spring中状态机的使用。

#### Spring Statemachine状态机

`Spring Statemachine`是`Spring`官方提供的一个框架，供应用程序开发人员在Spring应用程序中使用状态机。支持状态的嵌套（`substate`），状态的并行（`parallel`，`fork`，`join`）、子状态机等等。

官网地址：[https://projects.spring.io/spring-statemachine/](https://projects.spring.io/spring-statemachine/)

`Spring Statemachine`的设计是有状态的，每个`statemachine`实例内部保存了当前状态机上下文相关的属性（比如当前状态、上一次状态等），因此是*线程不安全的*，所以代码要么需要使用锁同步，要么需要用`ThreadLocal`，非常的痛苦和难用。因此，在实际场景中，基本上都是利用工厂创建状态机。

说到底`Spring StateMachine`上手难度非常大，如果没有用来做重型状态机的需求，十分不推荐普通的小项目进行接入。

案例：

https://blog.csdn.net/qq_22076345/article/details/109266901

https://www.jianshu.com/p/9ee887e045dd

#### COLA状态机

[实现一个状态机引擎，教你看清DSL的本质](https://blog.csdn.net/significantfrank/article/details/104996419)

- 首先，状态机的实现应该可以非常的轻量，最简单的状态机用一个Enum就能实现，基本是零成本。
- 其次，使用状态机的DSL来表达状态的流转，语义会更加清晰，会增强代码的可读性和可维护性。

开源状态机太复杂：就我们的项目而言（其实大部分项目都是如此）。我实在不需要那么多状态机的高级玩法：比如状态的嵌套（substate），状态的并行（parallel，fork，join）、子状态机等等。

开源状态机性能差：这些开源的状态机都是有状态的（Stateful）的，因为有状态，状态机的实例就不是线程安全的，而我们的应用服务器是分布式多线程的，所以在每一次状态机在接受请求的时候，都不得不重新build一个新的状态机实例。

COLA状态机设计的目标很明确，有两个核心理念：
- 简洁的仅支持状态流转的状态机，不需要支持嵌套、并行等高级玩法。
- 状态机本身需要是Stateless（无状态）的，这样一个Singleton Instance就能服务所有的状态流转请求了。

COLA状态机的核心概念如下图所示，主要包括：
- State：状态
- Event：事件，状态由事件触发，引起变化
- Transition：流转，表示从一个状态到另一个状态
- External Transition：外部流转，两个不同状态之间的流转
- Internal Transition：内部流转，同一个状态之间的流转
- Condition：条件，表示是否允许到达某个状态
- Action：动作，到达某个状态之后，可以做什么
- StateMachine：状态机


