springboot中websocket使用

#### 1.spring boot 原生 websocket

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

这里有几个注解需要注意一下，首先是他们的包都在 **javax.websocket**下。并不是`spring`提供的，而是`jdk`自带的，下面是他们的具体作用：

- `@ServerEndpoint`，通过这个`spring boot`就可以知道你暴露出去的`ws`应用的路径，有点类似我们经常用的`@RequestMapping`。比如你的启动端口是8080，而这个注解的值是ws，那我们就可以通过`ws://127.0.0.1:8080/ws`来连接你的应用；
- `@OnOpen`当`websocket`建立的连接断开后会触发这个注解修饰的方法，注意它有一个`Session`参数；
- `@OnMessage`当客户端发送消息到服务端时，会触发这个注解修改的方法，它有一个`String`入参表明客户端传入的值；
- `@OnError`当`websocket`建立连接时出现异常会触发这个注解修饰的方法，注意它有一个`Session`参数；

访问地址：http://localhost:8080/websocket/normal

#### 2.spring boot websocket + stomp

通过实现 WebSocketMessageBrokerConfigurer 接口和加上@EnableWebSocketMessageBroker来进行 stomp 的配置与注解扫描

```
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

        registry.setUserDestinationPrefix("/user/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket").withSockJS();
    }

}

```

- 通过实现`WebSocketMessageBrokerConfigurer`接口和加上`@EnableWebSocketMessageBroker`来进行`stomp`的配置与注解扫描；
- 其中覆盖`registerStompEndpoints`方法来设置暴露的`stomp`的路径，其它一些跨域、客户端之类的设置；
- 覆盖**configureMessageBroker**方法来进行节点的配置；
- 其中**enableSimpleBroker**配置的广播节点，也就是服务端发送消息，客户端订阅就能接收消息的节点；
- 覆盖**setApplicationDestinationPrefixes**方法，设置客户端向服务端发送消息的节点；
- 覆盖**setUserDestinationPrefix**方法，设置一对一通信的节点；
- 通过`@MessageMapping`来暴露节点路径，有点类似`@RequestMapping`。注意虽然写的是`msg/hellosingle`，但是我们客户端调用的真正地址是`/app/msg/hellosingle`。 因为我们在上面的`config`里配置了`registry.setApplicationDestinationPrefixes("/app")`；
- `@SendTo`这个注解会把返回值的内容发送给订阅了`/topic/hello`的客户端，与之类似的还有一个`@SendToUser`只不过他是发送给用户端一对一通信的。这两个注解一般是应答时响应的，如果服务端主动发送消息可以通过`simpMessagingTemplate`类的`convertAndSend`方法。注意`simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/greetings", msg)`，联系到我们上文配置的`registry.setUserDestinationPrefix("/user/")`,这里客户端订阅的是`/user/topic/greetings`,千万不要搞错；

访问测试地址：

```
// 系统通知发送地址
http://localhost:8080/stomp/socket/index
// 系统通知收听地址
http://localhost:8080/stomp/socket/info
// 用户1监听地址
http://localhost:8080/stomp/socket/msg/user1
// 用户2监听地址
http://localhost:8080/stomp/socket/msg/user2
```

#### 3.spring 封装

通过继承`TextWebSocketHandler`类并覆盖相应方法，可以对`websocket`的事件进行处理，这里可以同原生注解的那几个注解连起来看：
- `afterConnectionEstablished`方法是在`socket`连接成功后被触发，同原生注解里的`@OnOpen`功能；
- `afterConnectionClosed`方法是在`socket`连接关闭后被触发，同原生注解里的`@OnClose`功能；
- `handleTextMessage`方法是在客户端发送信息时触发，同原生注解里的`@OnMessage`功能；

通过`ConcurrentHashMap`来实现了一个`session`池，用来保存已经登录的`websocket`的`session`，服务端发送消息给客户端必须要通过这个`session`。

通过实现`HandshakeInterceptor`接口来定义握手拦截器，注意这里与上面`Handler`的事件是不同的，这里是建立握手时的事件，分为握手前与握手后，而`Handler`的事件是在握手成功后的基础上建立`socket`的连接。所以在如果把认证放在这个步骤相对来说最节省服务器资源。它主要有两个方法`beforeHandshake`与`afterHandshake`，顾名思义一个在握手前触发，一个在握手后触发。

通过实现`WebSocketConfigurer`类并覆盖相应的方法进行`websocket`的配置。我们主要覆盖`registerWebSocketHandlers`这个方法。通过向`WebSocketHandlerRegistry`设置不同参数来进行配置。其中`addHandler`方法添加我们上面的写的`ws`的`handler`处理类，第二个参数是你暴露出的`ws`路径。`addInterceptors`添加我们写的握手过滤器。`setAllowedOrigins("*")`这个是关闭跨域校验，方便本地调试，**线上推荐打开**。

#### 4.tio

```
<dependency>
    <groupId>org.t-io</groupId>
    <artifactId>tio-websocket-spring-boot-starter</artifactId>
    <version>3.6.0.v20200315-RELEASE</version>
</dependency>
```

需要实现`IWsMsgHandler`接口，并重写一些方法：
- `handshake`在握手的时候触发；
- `onAfterHandshaked`在握手成功后触发；
- `onBytes`客户端发送二进制消息触发；
- `onClose`客户端关闭连接时触发；
- `onText`客户端发送文本消息触发；

同时在启动类上需要添加`@EnableTioWebSocketServer`注解。

```
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTioWebSocketServer
public class WebSocketMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(WebSocketMain8080.class, args);
	}
	
	@Override
	public void run(String... args) {
		log.info("web socket 启动完毕，当前环境：{}", profileActive);
	}

}
```

#### 5.socketio

[netty-socketio](https://github.com/mrniko/netty-socketio/) 是一个开源的Socket.io服务器端的一个java的实现，它基于Netty框架，可用于服务端推送消息给客户端。

```
<dependency>
    <groupId>com.corundumstudio.socketio</groupId>
    <artifactId>netty-socketio</artifactId>
    <version>1.7.19</version>
</dependency>
```

注册Bean：
```
@Bean
public SocketIOServer socketIOServer() {
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
    config.setHostname(socketioProperties.getHost());
    config.setPort(socketioProperties.getPort());
    return new SocketIOServer(config);
}
```

项目启动后开启服务：
```
@Component
@Order(1)
@Slf4j
public class ServerRunner implements CommandLineRunner {

    private final SocketIOServer server;

    public ServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
        log.info("socket.io启动成功！");
    }

}
```

访问地址：http://localhost:8080/socketio/index

指定用户发送：http://localhost:8080/socketio/aaskskiwkks

所有用户发送消息：http://localhost:8080/socketio/send/notice

#### 5.netty-websocket-spring-boot-starter(第一节的扩展)

`spring-boot-starter-websocket`（或`spring-websocket`）。它可以让我们使用注解，很简单的进行`websocket`开发，让我们更多的关注业务逻辑。

它底层使用的是`tomcat`，且不说把整个`tomcat`放进一个`webSocket`服务中是否会太重，但在大数据量高并发的场景下，它的表现并不是非常理想。

`netty`一款高性能的NIO网络编程框架，在推送量激增时，表现依然出色。这得益于Netty的并发高、传输快、封装好等特点。但是，要在`springboot`项目中整合`netty`来开发`websocket`不是一件舒服的事，这会让你过多的关注非业务逻辑的实现。

`netty-websocket-spring-boot-starter`可以像`spring-boot-starter-websocket`一样使用注解进行开发，只需关注需要的事件(如`OnMessage`)。并且底层是使用`netty`,当需要调参的时候只需要修改配置参数即可，无需过多的关心`handler`的设置。

```
<dependency>
    <groupId>org.yeauty</groupId>
    <artifactId>netty-websocket-spring-boot-starter</artifactId>
    <version>0.12.0</version>
</dependency>
```

然后使用的方法基本与第一节一样（类路径不一样），配置信息都在`@ServerEndpoint`注解中配置，包括端口，地址等。

访问地址：http://localhost:8080/netty/websocket/index

#### 6.turf框架

地理空间分析库，处理各种地图算法

[turf官方中文地址](https://turfjs.fenxianglu.cn/)

[turf计算并集](https://www.nuomiphp.com/eplan/141755.html)

```
let unionize = (triangles) => {
    if(triangles.length == 0) {
        return null
    }
    let ret = triangles[0].feature
    triangles.forEach((t, index) => {
        if(index > 0) {
            ret = turf.union(t, t)
        }
    })
    return ret
}
```

[turf高德地图+vue实现页面点击绘制多边形及多边形切割拆分](http://www.zyiz.net/tech/detail-136608.html)
