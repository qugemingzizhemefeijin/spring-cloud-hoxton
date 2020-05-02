### SpringClound2020

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

#### 6.eureka、zookeeper、sonsul、Nacos异同点

| 组件名 | 语言 | CAP | 服务健康检查 | 对外暴露接口 | Spring Cloud集成 | 控制台管理 | 社区活跃度 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Eureka | Java | AP | 可配支持 | HTTP | 已集成 | 支持 | 低(2.0版本闭源) |
| Consul | GO | CP | 支持 | HTTP/DNS | 已集成 | 支持 | 高 |
| Zookeeper | Java | CP | 支持 | 客户端 | 已集成 | 不支持 | 红 |
| Nacos | Java | AP/CP | 支持 | HTTP | 已集成 | 支持 | 高

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

##### 10.Hystrix重要概念

1.服务降级(fallback)

>- 程序运行异常
>- 超时
>- 服务熔断触发服务降级
>- 线程池/信号量打满导致服务降级

2.服务熔断(break)

服务的降级->进而熔断->恢复调用链路

当失败的调用到一定阈值，缺省是5秒20次调用失败，就会启动熔断机制。熔断机制的注解是@HystrixCommand

3.服务限流(flowlimit)

高并发防止服务崩溃


##### 11.histrix相关知识点

如果使用feign的话，application.yml配置feign.hystrix.enable:true

@HystrixCommand内属性的修改建议重新启动微服物，而不是自动devtools热部署，可能会出现奇葩的问题。

@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")配置在Controller上可以开启全局的服务降级，如果@HystrixCommand没有特别指明降级策略，则默认使用@DefaultProperties的配置。

在@FeignClient属性中配置fallback并指定服务降级类，此类需要实现@FeignClient注解的那个接口即可。

>- 熔断打开:请求不再进行调用当前服务，内部设置时钟一般为MTTR(平均故障处理回见)，当打开时长达到所设时钟则进入半熔断状态。
>- 熔断关闭:熔断关闭不会对服务进行熔断。
>- 熔断半开:部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断。

涉及到断路器的三个重要参数：快照时间窗、请求总数阈值、错误百分比阈值。
>- 快照时间窗：断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒。
>- 请求总数阈值：在快照时间窗内，必须满足请求总数阈值才有资格熔断。默认为20，意味着在10秒内，如果该hystrix命令的调用次数不足20次，即使所有的请求都超时或其他原因失败，熔断器都不会打开。
>- 错误百分比阈值：当请求总数在快照时间内超过了阈值，比如发生了30次调用，如果在30次调用中，有15次发生了超时异常，也就是超时50%的错误百分比，在默认设定50%阈值情况下，这时候就会将熔断器打开。

如果需要使用hystrixDashboard，则需要在被监控项目中配置此功能。此功能是spring cloud升级后出先的问题。

```
/**
 * 此配置是为了服务监控而配置，与服务容错本身无关，springcloud升级后的坑
 * ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
 * 只要在自己的项目里配置上下面的servlet就可以了
 */
@Bean
public ServletRegistrationBean getServlet() {
	HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
	ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
	registrationBean.setLoadOnStartup(1);
	registrationBean.addUrlMappings("/hystrix.stream");
	registrationBean.setName("HystrixMetricsStreamServlet");
	return registrationBean;
}
```

##### 12.Gateway相关

Gateway三大概念：Route(路由)、Predicate(断言)、Filter(过滤)

Gateway服务不需要spring-boot-starter-web和spring-boot-starter-actuator，否则将会报错

spring.cloud.gateway.discovery.locator.enabled 开启从注册中心动态创建路由的功能，利用微服务名进行路由，uri: lb://cloud-payment-service，lb意思是动态路由。

gateway的断言有如下几种：After,Before,Between,Cookie,Header,Host,Method,Path,Query,ReadBodyPredicateFactory,RemoteAddr,Weight,CloudFoundryRouteService

gateway过滤器分为
>- 生命周期：pre前置过滤器，post后置通知
>- 种类：Gateway单一配置，GlobalFilter全局配置

##### 13.Config相关

主启动类上需要添加@EnableConfigServer

如果访问的是 http://config-3344.com:3344/master/config-dev.yml ，并且git下有search-paths，会首先去请求search-paths/config-dev.yml下的yml文件，如果根目录下有application.yml，则会将两个文件合并。

application.yml是用户级的资源配置，bootstrap.yml是系统级的，优先级最高。所以我们在用ConfigServer的时候，得把配置写到bootstrap.yml中，让其先加载ConfigServer等配置信息，再从git上获取配置中心的信息。

想让配置生效，手动更新必须通过 curl -X POST http://localhost:3355/actuator/refresh 来操作

cloud bus暂时只支持rabbitmq和kafka，使用cloud bus则需要将refresh接口操作config server，而不用的话则需要操作某个config client来传播刷新。

rabbitmq-plugins enable rabbitmq_management 安装管理插件

rabbit mq 默认登录地址 http://localhost:15672/ 用户名guest，密码guest

curl -X POST http://localhost:3344/actuator/bus-refresh 刷新config server配置并传播到所有的config client服务上。

如果要指定某一个实例生效而不是全部，使用 http://localhost:3344/actuator/bus-refresh/{destination} ，如 http://localhost:3344/actuator/bus-refresh/config-client:3355

##### 14.Stream相关知识

Spring Cloud Stream是一个构建消息驱动微服务的框架。Stream就是屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型。

应用程序通过inputs或者outputs来与Spring Cloud Stream中binder对象交互。<br>
通过我们配置来binding(绑定)，而Spring Cloud Stream的binder对象负责与消息中间件交互。<br>
所以，我们只需要搞清楚如何与Spring Cloud Stream交互就可以方便使用消息驱动的方式<br><br>
通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动。<br>
Spring Cloud Stream为一些供应商的消息中间件产品提供了个性化的自动化配置实现，引用了发布-订阅、消费组、分区的三个核心概念。<br>
目前仅支持RabbitMQ、Kafka。

[中文指导手册](https://m.wang1314.com/doc/webapp/topic/20971999.html)

| 组成 | 说明 |
| --- | --- |
| Middleware | 中间件，目前只支持RabbitMQ和Kafka |
| Binder | Binder是应用于消息中间件之间的封装，目前实现了Kafka和RabbitMQ的Binder，通过Binder可以很方便的连接中间件，可以动态的改变消息类型(对应于Kafka的Topic，RabbitMQ的exchange)，这些都可以通过配置文件来实现 |
| @Input | 注解标识输入通道，通过该输入通道接收到的消息进入应用程序 |
| @Output | 注解标识输出通过，发布的消息将通过该通道离开应用程序 |
| @StreamListener | 监听队列，用于消费者队列的消息接收 |
| @EnableBinding | 指信道Channel和exchange绑定在一起 |

rabbitMQ的消息如果持久化，则必须使用分组模式连接上服务。不同组之间会重复消费消息，同组会竞争消息。

##### 15.sleuth相关知识

Sleuth是分布式请求链路跟踪组件。

zipkin是链路跟踪Dashboard，[下载地址点击](https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/)，下载zipkin-server-2.12.9-exec.jar后，执行java -jar zipkin-server-2.12.9-exec.jar，启动成功后访问http://localhost:9411/zipkin/地址即可。

##### 16.spring cloud alibaba介绍

[中文文档](https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md)，[Spring.io Alibaba网站](https://spring.io/projects/spring-cloud-alibaba),[English Document](https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html)

##### 17.Nacos相关

[1.1.4版本下载地址](https://github.com/alibaba/nacos/releases/download/1.1.4/nacos-server-1.1.4.zip)，完成后，直接解压，执行bin目录下的startup.cmd，成功后访问[http://localhost:8848/nacos](http://localhost:8848/nacos)，用户名和密码均为nacos。

spring.cloud.nacos.discovery.server-addr=localhost:8848即可将服务注册到nacos。

| | Nacos | Eureka | Consul | CoreDNS | Zookeeper |
| --- | --- | --- | --- | --- | --- |
| 一致性协议 | CP/AP | AP | CP | - | CP |
| 健康检查 | TCP/HTTP/MySQL/Client Beat | Client Beat | TCP/HTTP/gRPC/Cmd | - | Client Beat |
| 负载均衡 | 权重/DSL/metadata/CMDB | Ribbon | Fabio | RR | - |
| 雪崩保护 | 支持 | 支持 | 不支持 | 不支持 | 不支持 |
| 自动注销实例 | 支持 | 支持 | 不支持 | 不支持 | 支持 |
| 访问协议 | HTTP/DNS/UDP | HTTP | HTTP/DNS | DNS | TCP |
| 监听支持 | 支持 | 支持 | 支持 | 不支持 | 支持 |
| 多数据中心 | 支持 | 支持 | 支持 | 不支持 | 不支持 |
| 跨注册中心 | 支持 | 不支持 | 支持 | 不支持 | 不支持 |
| SpringCloud集成 | 支持 | 支持 | 支持 | 不支持 | 不支持 |
| Dubbo集成 | 支持 | 不支持 | 不支持 | 不支持 | 支持 |
| K8S集成 | 支持 | 不支持 | 支持 | 支持 | 不支持 |

如果不需要存储服务级别的信息且服务实例是通过nacos-client注册，并能够保持心跳上报，阿么就可以选择AP模式。当前主流的服务如Spring Cloud和Dubbo服务，都适用于AP模式，AP模式为了服务的可用性而减弱了一致性，因此AP模式下只支持注册临时实例。

如果需要在服务级别编辑或者存在存储配置信息，那么CP是必须的，K8S服务和DNS服务则适用于CP模式。CP模式下则支持注册持久化实例，此时则是以Raft协议为集群运行模式，该模式下注册实例之前必须先注册服务如果服务不存在，则会返回错误。

Nacos的AP和CP切换：curl -X POST "$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP"

在 Nacos Spring Cloud 中，dataId 的完整格式如下：
```
${prefix}-${spring.profile.active}.${file-extension}
```

>- `prefix`默认为`spring.application.name`的值，也可以通过配置项`spring.cloud.nacos.config.prefix`来配置。
>- `spring.profile.active`即为当前环境对应的`profile`，详情可以参考[Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)。注意：当`spring.profile.active`为空时，对应的连接符`-`也将不存在，`dataId`的拼接格式变成`${prefix}.${file-extension}`。
>- `file-exetension`为配置内容的数据格式，可以通过配置项`spring.cloud.nacos.config.file-extension`来配置。目前只支持`properties`和`yaml`类型。

最外层Namespace(命名空间)是可以用于区分部署环境，Group和DataID逻辑上区分两个目标对象。默认情况Namespace=public,Group=DEFAULT_GROUP,默认Cluster是DEFAULT。<br>
比方说我们现在有三个环境，开发、测试、生产环境，我们可以创建三个Namespace，不同的Namespace之间是隔离的。<br>
Group默认是DEFAUTL_GROUP，Group可以把不同的微服务划分到同一个分组里面去<br>
Service就是微服务，一个Service而已包含多个Cluster(集群)，Nacos默认Cluster是DEFAULT，Cluster是对指定微服务的一个虚拟划分。<br>
比方说为了容灾，见Service微服务分别部署在杭州机房和广州机房，这时就可以给杭州机房的Service微服务起一个集群名称(HZ)，给广州机房的Service微服务起一个集群名称(GZ)，还可以尽量让同一个机房的微服务互相调用，以提升性能。

最后是Instance，就是微服务的实例。

##### 18.Nacos安装

安装Mysql，下载[nacos-server-1.1.4.tar.gz](https://github.com/alibaba/nacos/releases/download/1.1.4/nacos-server-1.1.4.tar.gz)包

1.设置mysql

```
create database nacos_test DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
GRANT USAGE,CREATE,SELECT,SHOW VIEW,CREATE TEMPORARY TABLES,DELETE,INSERT,UPDATE,DROP,INDEX,ALTER ON `nacos_test`.* TO 'nacos'@'127.0.0.1' IDENTIFIED BY 'nacos#^12A34';
```

2.解压nacos安装包

```
tar -zxvf nacos-server-1.1.4.tar.gz
mv nacos /usr/local/
cd /usr/local/nacos/conf
mysql -u root -p nacos_test < nacos-mysql.sql #导入数据
```

3.修改配置文件

vi /usr/local/nacos/conf/application.properties 新增如下内容：

```
#*************** Config Module Related Configurations ***************#
### If user MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_test?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=nacos
db.password=nacos#^12A34
```

4.修改nginx配置(直接使用gitlab的nginx)

vi /var/opt/gitlab/nginx/conf/file/http/http.conf

```
upstream nacos{
        server  127.0.0.1:8848;
        server  127.0.0.1:8849;
        server  127.0.0.1:8850;
}

server {
        listen 80;
        server_name
                nacos.xxxx.cn
        ;

        client_max_body_size 200m;
        access_log /var/log/gitlab/nginx/nacos_access.log newdata;
        error_log /var/log/gitlab/nginx/nacos_error.log ;

        location / {
                proxy_pass      http://nacos;
                proxy_set_header    Host    $http_host;
                proxy_set_header    X-Real-IP   $remote_addr;
                proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
        }
}
```

5.修改start.sh文件(因为是在一台机器)

主要是为了让nacos支持-p命令，省得拷贝出N个nacos来做测试

vi /usr/local/nacos/bin/startup.sh

```
//57行配置如下

while getopts ":m:f:s:p:" opt
do
    case $opt in
        m)  
            MODE=$OPTARG;;
        f)  
            FUNCTION_MODE=$OPTARG;;
        s)  
            SERVER=$OPTARG;;
        p)  
            PORT=$OPTARG;;
        ?)  
        echo "Unknown parameter"
        exit 1;; 
    esac
done

//最后三行替换
echo "$JAVA -Dserver.port=${PORT} ${JAVA_OPT}" > ${BASE_DIR}/logs/start.out 2>&1 &
nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
echo "nacos is starting，you can check the ${BASE_DIR}/logs/start.out"
```

mkdir /usr/local/nacos/logs	#创建日志目录

6.设置cluster.conf

```
cp /usr/local/nacos/conf/cluster.conf.example /usr/local/nacos/conf/cluster.conf
cat>/usr/local/nacos/conf/cluster.conf<<EOF
172.17.0.14:8848
172.17.0.14:8849
172.17.0.14:8850
EOF
```

此处主要，填写自己的局域网的IP，不要填写127.0.0.1

7.启动集群

```
/usr/local/nacos/bin/startup.sh -p 8848
/usr/local/nacos/bin/startup.sh -p 8849
/usr/local/nacos/bin/startup.sh -p 8850
```

##### 18.Sentinel介绍

[中文介绍文档](https://github.com/alibaba/sentinel/wiki/%E4%BB%8B%E7%BB%8D)，[中文使用文档](https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8)

Sentinel 具有以下特征:
>- 丰富的应用场景：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。
>- 完备的实时监控：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
>- 广泛的开源生态：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
>- 完善的SPI扩展点：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。

Hystrix痛点：
>- 需要程序员自己手工搭建监控平台，监控平台对中文不友好。
>- 没有一套web界面可以给我们进行更加细粒度化的配置、流控、速率控制、服务熔断、服务降级...

Sentinel优点：
>- 单独一个组件，可以独立出来（其实Hystrix也可以单独出来）
>- 直接界面化的细粒度统一配置

[下载地址点击](https://github.com/alibaba/Sentinel/releases/download/1.7.0/sentinel-dashboard-1.7.0.jar)

启动命令:java -jar sentinel-dashboard-1.7.0.jar

默认用户名和密码都是sentinel

项目在集成了sentinel后，没有在 http://localhost:8080/#/dashboard/home 中看到项目信息，是因为使用的是懒加载机制，只要执行依次访问就可以看到了。

***流控规则介绍：***
* 资源名：唯一名称，默认请求路径
* 针对来源：Sentinel可以针对调用者进行限流，填写微服务名，默认defautl（不区分来源）
* 阈值类型/单机阈值：
	* QPS(每秒钟的请求数量)：当调用该api的QPS达到阈值的时候，进行限流
	* 线程数：当调用该api的线程数达到阈值的时候，进行限流
* 是否集群：不需要集群
* 流控模式：
	* 直接：api达到限流条件时，直接限流
	* 关联：当关联的资源达到阈值时，就限流自己
	* 链路：只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就进行限流）【API级别的针对来源】
* 流控效果：
	* 快速失败：直接失败，抛异常
	* Warm UP：根据codeFactor（冷加载因子，默认3）的值，从阈值/codeFactor，经过预热时长，才达到设置的QPS阈值
	* 排队等待：匀速排队，让请求以匀速的速度通过，阈值类型必须设置为QPS，否则无效

这里有个问题，使用链路限流好像不起作用。[查看此issues](https://github.com/alibaba/Sentinel/issues/1213)，我这边使用了`spring-cloud-alibaba 2.1.0.RELEASE`和`spring-cloud-alibaba 2.1.2.RELEASE`都没有测试成功，`spring-cloud-alibaba 2.1.1.RELEASE`是可以按照内容测试成功了。

WarmUp配置相关信息：

默认coldFactor为3，即请求QPS从(threshold / 3)开始，经多少预热时长才逐渐升至设定的QPS阈值。如，阈值为10并且预热时长设置为5秒，系统初始化的阈值为10/3约等于3，即阈值刚开始为3；然后过了5秒偶阈值才慢慢升高恢复到10。可以查看此类`com.alibaba.csp.sentinel.slots.block.flow.controller.WarmUpController`


***降级规则介绍：***
* RT(平均响应时间，秒级)
	* 平均响应时间 超出阈值 且 在时间窗口内通过的请求>=5，两个条件同时满足后出发降级
	* 窗口期过后关闭断路器
	* RT最大4900（更大的需要通过 -Dcsp.sentinel.statistic.max.rt=N才能生效）
* 异常比例(秒级)
	* QPS >= 5 且异常比例(秒级统计)超过阈值时，触发降级；时间窗口结束后，关闭降级。
* 异常数(分钟级)
	异常数(分钟统计)超过阈值时，触发降级；时间窗口结束后，关闭降级。时间窗口一定要大于等于60秒。
	
Sentinel熔断降级会在调用链路中某个资源出现不稳定状态时（例如调用超时或异常比例升高），对这个资源的调用进行限制，让请求快速失败，避免影响到其它的资源而导致级联错误。

当资源被降级后，在接下来的降级时间窗口之内，对该资源的调用都自动熔断（默认行为是抛出DegradeException）。Sentinel的熔断器是没有半开状态的。

热点参数限流规则在限流方法上`@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")`，请务必添加blockHandler，否则在访问方法到达限流的时候将抛出Exception异常并显示/error界面。

@SentinelResource 处理的是Sentinel控制台配置的违规情况，有blockHandler方法配置的兜底处理；<br>
RuntimeException int age=10/0，这个是java运行时报出的运行时异常RunTimeException，@SentinelResource不管。<br>
@SentinelResource主管配置出错，运行出错该走异常走异常。

系统保护规则是从应用级别的入口流量进行控制，从单台机器的load、CPU使用率、平均RT、入口QPS和并发线程数等几个维度监控应用指标，让系统尽可能跑到最大吞吐量的同时保证系统整体的稳定性。

系统保护规则是应用整体维度的，而不是资源维度的，并且仅对入口流量生效。入口流量指的是进入应用的流量（EntryType.IN），比如Web服务或Dubbo服务端接收的请求，都属于入口流量。

系统规则支持以下的模式：
* Load自适应（仅对Linux/Unix机器生效）：系统的load1作为启发指标，进行自适应系统保护。当系统load1超过设定的启发值，且系统当前的并发线程数超过估算的系统容量时才会触发系统保护（BBR阶段）。系统容量由系统的`maxOPS`*`minRT`估算得出。设定参考值一般是`CPU CORES * 2.5`。
* CPU USAG(1.5.0+版本)：当系统CPU使用率超过阈值即触发系统保护（取值范围0.0-1.0），比较灵敏。
* 平均RT：当单台机器上所有入口流量的平均RT达到阈值即触发系统保护，单位是毫秒。
* 并发线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
* 入口QPS：当单台机器上所有入口流量的QPS达到阈值即触发系统保护。

| | Sentinel | Hystrix | resilience4j |
| --- | --- | --- | --- |
| 隔离策略 | 信号量隔离(并发线程数限流) | 线程池隔离/信号量隔离 | 信号量隔离 |
| 熔断降级策略 | 基于响应时间、异常比率、异常数 | 基于异常比率 | 基于异常比率、响应时间 |
| 实时统计实现 | 滑动窗口(LeapArray) | 滑动窗口(基于RxJava) | Ring Bit Buffer |
| 动态规则配置 | 支持多种数据源 | 支持多种数据源 | 有限支持 |
| 扩展性 | 多个扩展点 | 插件的形式 | 接口的形式 |
| 基于注解的支持 | 支持 | 支持 | 支持 |
| 限流 | 基于QPS、支持基于调用关系的限流 | 有限的支持 | Rate Limiter |
| 流量整形 | 支持预热模式、匀速器模式、预热排队模式 | 不支持 | 简单的Rate Limiter模式 |
| 系统自适应保护 | 支持 | 不支持 | 不支持 |
| 控制台 | 提供开箱即用的控制台、可配置规则、查看秒级监控、机器发现等 | 简单的监控查看 | 不提供控制台，可对接其它监控系统 |

Sentinel的规则持久化到nacos中需要添加如下信息：

```
//pom文件新增
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>

//application.yml增加如下配置
spring: 
  cloud: 
    sentinel: 
      datasource: # 将sentinel流控规则持久化到nacos中
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: cloudalibaba-sentinel-service
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow
            
//去nacos配置中心的配置列表添加+号,DataId:cloudalibaba-sentinel-service,配置格式JSON,配置内容如下：
[
    {
        "resource": "/rateLimit/byUrl", 
        "limitApp": "default", 
        "grade": 1, 
        "count": 1, 
        "strategy": 0, 
        "controlBehavior": 0, 
        "clusterMode": false
    }
]
//resource:资源名称
//limitApp:来源应用
//grade:阈值类型，0表示线程数，1表示QPS
//count:单机阈值
//strategy:流控模式，0表示直接，1表示关联，2表示链路
//controlBehavior:流控效果，0表示快速失败，1表示Warm UP，2表示排队等待
//clusterMode:是否集群
```

##### 19.Seata相关知识

Seata是一款开源的分布式事务解决方案，致力于在微服务架构下提供高性能和简单易用的分布式事务服务。[中文官方网站](http://seata.io/zh-cn/)

分布式事务处理过程为TXID + 三组件模型：
>- TXID (Transaction ID XID)，全局唯一的事务ID。
>- TC (Transaction Coordinator) - 事务协调者，维护全局和分支事务的状态，驱动全局事务提交或回滚。
>- TM (Transaction Manager) - 事务管理器，定义全局事务的范围：开始全局事务、提交或回滚全局事务。
>- RM (Resource Manager) - 资源管理器，管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

Seata处理过程：
>- 1. TM向TC申请开启一个全局事务，全局事务创建成功并生成一个全局唯一的XID；
>- 2. XID在微服务调用链路的上下文中传播；
>- 3. RM向TC注册分支事务，将其纳入XID对应全局事务的管辖；
>- 4. TM向TC发起针对XID的全局提交或回滚决议；
>- 5. TC调度XID下管辖的全部分支事务完成提交或回滚。

分布式事务执行流程：
>- 1. TM开启分布式事务（TM向TC注册全局事务记录）；
>- 2. 按业务场景，编排数据库、服务等事务内资源（RM向TC汇报资源准备状态）；
>- 3. TM结束分布式事务，事务一阶段结束（TM通知TC提交/回滚分布式事务）；
>- 4. TC汇总事务信息，决定分布式事务是提交还是回滚；
>- 5. TC通知所有RM提交/回滚资源，事务二阶段结束。

安装流程如下：

1.下载

从[https://github.com/seata/seata/releases](https://github.com/seata/seata/releases)下载相关版本，我这里下载的是v0.9.0.zip。

2.安装

下载后解压到指定目录并修改conf目录下的file.conf配置文件，先备份原文件。主要修改内容：自定义事务组名称+事务日志存储模式为DB+数据库连接信息

```
service {
  #vgroup->rgroup
  vgroup_mapping.cg_tx_group = "default"	//此处需要修改一下名称
  #only support single node
  default.grouplist = "127.0.0.1:8091"
  #degrade current not support
  enableDegrade = false
  #disable
  disable = false
  #unit ms,s,m,h,d represents milliseconds, seconds, minutes, hours, days, default permanent
  max.commit.retry.timeout = "-1"
  max.rollback.retry.timeout = "-1"
}

store {
  ## store mode: file、db
  mode = "db"	//此处改成db
  ...
  
  //这里修改数据库连接
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
    datasource = "dbcp"
    ## mysql/oracle/h2/oceanbase etc.
    db-type = "mysql"
    driver-class-name = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://127.0.0.1:3306/seata"	//这里记得更改连接，用户名和密码
    user = "root"
    password = "abcd1234"
    min-conn = 1
    max-conn = 3
    global.table = "global_table"
    branch.table = "branch_table"
    lock-table = "lock_table"
    query-limit = 100
  } 
}
```

下面创建数据库信息
```
create database seata default charset utf8mb4 collate utf8mb4_general_ci;
```

导入db_store.sql数据文件到seata数据库中。执行`mysql -u root -p seata < db_store.sql`即可

备份conf目录下的registry.conf文件，修改此文件的内容：
```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"	//将file修改成nacos

  nacos {
    serverAddr = "localhost:8848"	//此处地址修改
    namespace = ""
    cluster = "default"
  }
  ...
}
```

3.启动seata

先启动nacos服务，再启动seata服务

##### 20.测试seata分布式事务

建表语句如下：
```
create database seata_order default charset utf8mb4 collate utf8mb4_general_ci;	//存储订单的数据库
create database seata_storage default charset utf8mb4 collate utf8mb4_general_ci; //存储库存的数据库
create database seata_account default charset utf8mb4 collate utf8mb4_general_ci; //存储账户信息的数据库

use seata_order;
create table `t_order`(
`id` BIGINT(11) not null auto_increment primary key,
`user_id` bigint(11) comment '用户ID',
`product_id` bigint(11) comment '产品ID',
`count` int(10) comment '数量',
`money` decimal(11,0) comment '金额',
`status` int(1) comment '订单状态:0创建中,1已完成'
) engine=innodb default charset utf8mb4 collate utf8mb4_general_ci comment '订单表';

use seata_storage;
create table t_storage(
`id` BIGINT(11) not null auto_increment primary key,
`product_id` bigint(11) comment '产品ID',
`total` int(10) comment '总库存',
`used` int(10) comment '已用库存',
`residue` int(10) comment '剩余库存'
) engine=innodb default charset utf8mb4 collate utf8mb4_general_ci comment '库存表';

insert into t_storage values(1,1,100,0,100);

use seata_account;
create table t_account(
`id` BIGINT(11) not null auto_increment primary key,
`user_id` bigint(11) comment '用户ID',
`total` decimal(10,0) comment '总额度',
`used` decimal(10,0) comment '已用额度',
`residue` decimal(10,0) comment '剩余额度'
) engine=innodb default charset utf8mb4 collate utf8mb4_general_ci comment '账户表';

insert into t_account values(1,1,1000,0,1000);
```

订单-库存-账户3个库下都需要创建各自的回滚日志表，在seata的conf目录的db_undo_log.sql，直接执行即可。

```
use seata_order;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

use seata_storage;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

use seata_account;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```







