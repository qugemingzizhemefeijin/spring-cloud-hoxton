[Spring WebFlux 官网地址](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux)

### 什么是spring webflux？

`Spring MVC`构建于`Servlet API`之上，使用的是同步阻塞式`I/O`模型，什么是同步阻塞式 I/O 模型呢？就是说，每一个请求对应一个线程去处理。

`Spring WebFlux`是一个异步非阻塞式的`Web`框架，它能够充分利用多核`CPU`的硬件资源去处理大量的并发请求。

### WebFlux 的优势&提升性能?

`WebFlux`内部使用的是响应式编程（`Reactive Programming`），以`Reactor`库为基础, 基于异步和事件驱动，可以让我们在不扩充硬件资源的前提下，提升系统的吞吐量和伸缩性。看到这里，你是不是以为`WebFlux`能够使程序运行的更快呢？量化一点，比如说我使用`WebFlux`以后，一个接口的请求响应时间是不是就缩短了呢？

抱歉了，答案是否定的！`WebFlux`并不能使接口的响应时间缩短，它仅仅能够提升吞吐量和伸缩性。

### WebFlux 应用场景

`Spring WebFlux`是一个异步非阻塞式的`Web`框架，所以，它特别适合应用在`IO`密集型的服务中，比如微服务网关这样的应用中。

> IO密集型包括：磁盘IO密集型, 网络IO密集型，微服务网关就属于网络IO密集型，使用异步非阻塞式编程模型，能够显著地提升网关对下游服务转发的吞吐量。

### 选 WebFlux 还是 Spring MVC?

首先你需要明确一点就是：`WebFlux`不是`Spring MVC`的替代方案！，虽然`WebFlux`也可以被运行在`Servlet`容器上（需是`Servlet 3.1+`以上的容器），但是`WebFlux`主要还是应用在异步非阻塞编程模型，而`Spring MVC`是同步阻塞的，如果你目前在`Spring MVC`框架中大量使用非同步方案，那么，`WebFlux`才是你想要的，否则，使用`Spring MVC`才是你的首选。

在微服务架构中，`Spring MVC`和`WebFlux`可以混合使用，比如已经提到的，对于那些`IO`密集型服务(如网关)，我们就可以使用`WebFlux`来实现。

### WebFlux 和 Spring MVC 的异同点

相同点：
- 都可以使用`Spring MVC`注解，如`@Controller`, 方便我们在两个`Web`框架中自由转换；
- 均可以使用`Tomcat`, `Jetty`,`Undertow Servlet`容器（`Servlet 3.1+`）；

注意点：
- `Spring MVC`因为是使用的同步阻塞式，更方便开发人员编写功能代码，`Debug`测试等，一般来说，如果`Spring MVC`能够满足的场景，就尽量不要用`WebFlux`;
- `WebFlux`默认情况下使用`Netty`作为服务器;
- `WebFlux`不支持`MySql`;

`Spring MVC`的前端控制器是`DispatcherServlet`, 而`WebFlux`是`DispatcherHandler`，它实现了`WebHandler`接口。
