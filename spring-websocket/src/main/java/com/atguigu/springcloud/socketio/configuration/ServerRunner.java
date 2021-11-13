package com.atguigu.springcloud.socketio.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 在项目服务启动的时候启动socket.io服务
 */
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
