### springboot中log4j2的一些配置和使用

配置了`RingBuffer`的大小，抛弃策略等，在`log4j2.component.properties`中配置。

自定义异常输出插件，对前缀的包的信息进行输出，参考了此项目[spring-cloud-parent](https://github.com/JoJoTec/spring-cloud-parent)

Disruptor的原理分析：

- 使用循环数组代替队列生产者消费者模型自然是离不开队列的，使用预先填充数据的方式来避免 GC；
- 使用CPU缓存行填充的方式来避免极端情况下的数据争用导致的性能下降；
- 多线程编程中尽量避免锁争用的编码技巧。

[《disruptor笔记》系列链接](https://blog.csdn.net/boling_cavalry/article/details/117636483)

