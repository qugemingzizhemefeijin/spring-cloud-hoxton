### springboot中log4j2的一些配置和使用

配置了`RingBuffer`的大小，抛弃策略等，在`log4j2.component.properties`中配置。

自定义异常输出插件，对前缀的包的信息进行输出，参考了此项目[spring-cloud-parent](https://github.com/JoJoTec/spring-cloud-parent)

Disruptor的原理分析：

- 使用循环数组代替队列生产者消费者模型自然是离不开队列的，使用预先填充数据的方式来避免 GC；
- 使用CPU缓存行填充的方式来避免极端情况下的数据争用导致的性能下降；
- 多线程编程中尽量避免锁争用的编码技巧。

handleEventsWith和handleEventsWithWorkerPool方法的区别：

>- disruptor.handleEventsWith(EventHandler ... handlers)，将多个EventHandler的实现类传入方法，封装成一个EventHandlerGroup，实现多消费者消费。
>- disruptor.handleEventsWithWorkerPool(WorkHandler ... handlers)，将多个WorkHandler的实现类传入方法，封装成一个EventHandlerGroup实现多消费者消费。

两者共同点都是，将多个消费者封装到一起，供框架消费消息。

不同点在于：

>- 对于某一条消息m，handleEventsWith方法返回的EventHandlerGroup，Group中的每个消费者都会对m进行消费，各个消费者之间不存在竞争。handleEventsWithWorkerPool方法返回的EventHandlerGroup，Group的消费者对于同一条消息m不重复消费；也就是，如果c0消费了消息m，则c1不再消费消息m。
>- 传入的形参不同。对于独立消费的消费者，应当实现EventHandler接口。对于不重复消费的消费者，应当实现WorkHandler接口。

### RingBuffer

**RingBuffer的优点：**

之所以RingBuffer采用这种数据结构，是因为它在可靠消息传递方面有很好的性能。这就够了，不过它还有一些其他的优点。

首先，因为它是数组，所以要比链表快，而且有一个容易预测的访问模式。（译者注：数组内元素的内存地址的连续性存储的）。这是对CPU缓存友好的—也就是说，在硬件级别，数组中的元素是会被预加载的，因此在ringbuffer当中，cpu无需时不时去主存加载数组中的下一个元素。（校对注：因为只要一个元素被加载到缓存行，其他相邻的几个元素也会被加载进同一个缓存行）

其次，你可以为数组预先分配内存，使得数组对象一直存在（除非程序终止）。这就意味着不需要花大量的时间用于垃圾回收。此外，不像链表那样，需要为每一个添加到其上面的对象创造节点对象—对应的，当删除节点时，需要执行相应的内存清理操作。

**RingBuffer底层实现：**

RingBuffer是一个首尾相连的环形数组，所谓首尾相连，是指当RingBuffer上的指针越过数组是上界后，继续从数组头开始遍历。因此，RingBuffer中至少有一个指针，来表示RingBuffer中的操作位置。另外，指针的自增操作需要做并发控制，Disruptor和本文的OptimizedQueue都使用CAS的乐观并发控制来保证指针自增的原子性。

Disruptor中的RingBuffer上只有一个指针，表示当前RingBuffer上消息写到了哪里，此外，每个消费者会维护一个sequence表示自己在RingBuffer上读到哪里，从这个角度讲，Disruptor中的RingBuffer上实际有消费者数+1个指针。由于我们要实现的是一个单消息单消费的阻塞队列，只要维护一个读指针（对应消费者）和一个写指针（对应生产者）即可，无论哪个指针，每次读写操作后都自增一次，一旦越界，即从数组头开始继续读写。

### 参考

[《disruptor笔记》系列链接](https://blog.csdn.net/boling_cavalry/article/details/117636483)

