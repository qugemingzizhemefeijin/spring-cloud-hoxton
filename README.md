### SpringClound2020

尚硅谷阳哥springcloud第二季视频代码资料

Hoxton.SR1 + Springboot 2.2.2.RELEASE + Nacos等等


##### 1.要使用DiscoveryClient需要注意的

需要在主启动类上添加@EnableDiscoveryClient注解

##### 2.Eureka的自我保护机制？

CAP理论中的AP。

某时刻一个微服务可用了，Eureka不会立刻清理，依旧会对该微服务的信息进行保存。
为了防止EurekaClient可以正常运行，但是与EurekaServer网络不通情况下，EurekaServer不会立刻将EurekaClient服务剔除。

默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServeriang会注销该实例（默认90秒）。但是当网络分区故障发生（延时、卡顿、拥挤）时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了--因为微服务本身其实是健康的，此时本不应该注销这个服务。Eureka通过“自我保护机制”来解决这个问题--当EurekaServer节点在短时间内丢失过多客户端时（可能发生了网络分区故障），那么这个节点就会进入自我保护模式。

在自我保护模式中，EurekaServer会保护服务注册表中的信息，不再注销任何服务实例。

##### 3.关闭Eureka的自我保护机制？

eureka.server.enable-self-preservation=false	则可以关闭掉，默认是true
eureka.server.eviction-interval-timer-in-ms=2000	当微服务2秒没有发心跳，则踢出

eureka.instance.lease-renewal-interval-in-seconds	Eureka客户端向服务端发送心跳的时间间隔，单位为秒(默认是30秒)
eureka.instance.lease-expiration-duration-in-seconds	Eureka服务端在收到最后一次心跳后等待时间上限，单位为秒(默认是90秒)，超时将剔除服务

##### 4.spring cloud在zk上注册是节点是哪种类型的？

是临时节点

##### 5.consul启动

consul agent -dev

#### 6.eureka、zookeeper、sonsul异同点

| 组件名 | 语言 | CAP | 服务健康检查 | 对外暴露接口 | Spring Cloud集成 |
| Eureka | Java | AP | 可配支持 | HTTP | 已集成 |
| Consul | GO | CP | 支持 | HTTP/DNS | 已集成 |
| Zookeeper | Hava | CP | 支持 | 客户端 | 已集成 |

最多只能同时较好的满足两个。
CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性、可用性和分区容错性三个需求，因此，根据CAP原理将NoSQL数据库分成了满足CA原则，满足CP原则和满足AP原则三大类：
>- CA-单点集群，满足一致性，可用性的系统，通常在可扩展性行不太强大；
>- CP-满足一致性，分区容错性的系统，通常性能不是特别高；
>- AP-满足可用性，分区容错性的系统，通常可能对一致性要求低一些；

##### 7.RestTemplate使用getForObject与getForEntity区别？

getForObject返回对象为响应体中数据转化成的对象，基本可以理解为JSON

getForEntity返回对象为ResponseEntity对象，包含了相应中的一些重要信息，如响应头、相应状态码、响应体等

##### 8.Ribbon默认自带的负载规则(IRule)

>- RoundRobinRule 默认，轮询
>- RandomRule 随机
>- RetryRule 先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务
>- BestAvailableRule 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
>- AvailabilityFilteringRule 先过滤故障实例，再选择并发较小的实例
>- WeightedResponseTimeRule 对RoundRobinRule的扩展，相应速度越快的实例选择权重越大，越容易被选择
>- ZoneAvoidanceRule 复合判断server所在区域的性能和server的可用性选择服务器

自定义Ribbon规则类不能放在@ComponentScan所扫描的当前包下以及子包下

##### 9.OpenFeign的日志级别

>- None:默认的，不显示任何日志；
>- BASIC:仅记录请求方法、URL、响应状态码及执行时间；
>- HEADERS:除了BASIC中定义的信息之外，还有请求和响应的头信息；
>- FULL:除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据；




