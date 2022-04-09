### Guava And CAFFEINE 使用案例

`Caffeine`是在`guava`基础上进行优化的产物，也是带着替代`guava`的目的而来的，因而在使用上差别不大。但是性能的话，`caffeine`要比`guava`的要好。[网上的一篇文章](https://blog.csdn.net/zhangyunfeihhhh/article/details/108105928) 显示了其性能对比。

[Redis+Caffeine两级缓存案例](https://www.heapdump.cn/article/3537855)

#### 加载方式的区别

`guava`初始化时重写的`load()`方法，不能返回`null`值，但`caffeine`可以。

`caffeine cache`还提供了异步加载方式。

#### 基于时间的过期策略

`guava`和`caffeine`都支持通过两种策略来进行数据的回收策略，分别是`expireAfterWrite`、`expireAfterAccess`，此外`caffeine`还支持通过`expireAfter`来通过重新相关方法自定义过期策略，这些过期策略都是初始化时进行指定。

#### 基于大小的驱逐策略

无论是`caffeine`还是`guava`，通过设置过期时间，是无法使缓存值从缓存中驱逐出去的，只会在指定时间后被新值替代，所以，在使用`caffeine`或者`guava`的时候，设置`maximumSize`是很有必要的。

`caffeine`和`guava`也是在`get`或者`put`操作的时候根据设置的大小进行清除的，但是两者的清除算法存在区别，`guava`使用`LRU`算法进行实现，而`caffeine`使用综合`LFU`和`LRU`优点产生的`W-TinyLFU`进行数据清除，改良的算法可以更科学的进行非热点数据的驱逐，较大程度的增加缓存的命中率。

