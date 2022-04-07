package com.atguigu.springcloud.server;

import com.atguigu.springcloud.service.MyGrpcServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GrpcServer {

    private final int port = 50051;//grpc服务端口
    private Server server;//grpc server

    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        //指定grpc服务器端口、接口服务对象，启动grpc服务器
        server = ServerBuilder.forPort(port)
                .addService(new MyGrpcServiceImpl())
                .build().start();
        log.info("service start...");
        //添加停机逻辑
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("*** shutting down gRPC server since JVM is shutting down");
            GrpcServer.this.stop();
            log.error("*** server shut down");
        }));
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

}
