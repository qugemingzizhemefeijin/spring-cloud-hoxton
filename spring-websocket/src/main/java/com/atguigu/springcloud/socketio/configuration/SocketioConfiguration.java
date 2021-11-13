package com.atguigu.springcloud.socketio.configuration;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class SocketioConfiguration {

    @Autowired
    private SocketioProperties socketioProperties;

    @Bean
    public SocketIOServer socketIOServer() throws IOException {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        // 开启Socket端口复用
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);

        // 开启ssl
        if(socketioProperties.getKeyStore() != null) {
            config.setKeyStorePassword(socketioProperties.getKeyStore().getPassword());
            config.setKeyStore(new ClassPathResource(socketioProperties.getKeyStore().getFilePath()).getInputStream());
        }

        config.setHostname(socketioProperties.getHost());
        config.setPort(socketioProperties.getPort());
        // 连接数大小
        config.setWorkerThreads(socketioProperties.getWorkCount());
        //允许客户请求
        config.setAllowCustomRequests(socketioProperties.isAllowCustomRequests());
        //协议升级超时时间(毫秒)，默认10秒，HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(socketioProperties.getUpgradeTimeout());
        //Ping消息超时时间(毫秒)，默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(socketioProperties.getPingTimeout());
        //Ping消息间隔(毫秒)，默认25秒。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(socketioProperties.getPingInterval());
        //设置HTTP交互最大内容长度
        config.setMaxHttpContentLength(socketioProperties.getMaxHttpContentLength());
        //设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
        config.setMaxFramePayloadLength(socketioProperties.getMaxFramePayloadLength());
        config.setTransports(Transport.POLLING, Transport.WEBSOCKET);


        /*config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
            @Override
            public boolean isAuthorized(HandshakeData data) {
                //http://localhost:8081?username=test&password=test
                //例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息
                // String username = data.getSingleUrlParam("username");
                // String password = data.getSingleUrlParam("password");
                return true;
            }
        });*/

        // 这里还可以将不同的链接配置到不同的命名空间中
        // final SocketIONamespace chat1namespace = server.addNamespace("/chat1");
        // chat1namespace.addEventListener("message", ChatObject.class, new DataListener<ChatObject>() {
        //      @Override
        //      public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
        //         // broadcast messages to all clients
        //         chat1namespace.getBroadcastOperations().sendEvent("message", data);
        //    }
        // });

        return new SocketIOServer(config);
    }

    /**
     * tomcat启动时候，扫码socket服务器并注册
     * @param socketServer SocketIOServer
     * @return SpringAnnotationScanner
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
