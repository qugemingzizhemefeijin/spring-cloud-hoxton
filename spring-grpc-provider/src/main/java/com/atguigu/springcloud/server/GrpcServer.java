package com.atguigu.springcloud.server;

import com.atguigu.springcloud.grpc_api.Grpc;
import com.atguigu.springcloud.grpc_api.GrpcServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
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
                .addService(new GreeterImpl())
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

    //内部类，继承抽象类 GrpcServiceGrpc.GrpcServiceImplBase，
    //并重写服务方法 sendUnaryRequest
    private class GreeterImpl extends GrpcServiceGrpc.GrpcServiceImplBase {
        //UnaryRequest 客户端请求参数，
        //StreamObserver<UnaryResponse> 返回给客户端的封装参数
        public void sendUnaryRequest(Grpc.UnaryRequest request, StreamObserver<Grpc.UnaryResponse> responseObserver) {
            ByteString message = request.getData();
            log.info("server, serviceName:" + request.getServiceName()
                    + "; methodName:" + request.getMethodName() + "; datas:" + new String(message.toByteArray()));
            Grpc.UnaryResponse.Builder builder = Grpc.UnaryResponse.newBuilder();
            builder.setServiceName("GrpcServiceResponse").setMethodName("sendUnaryResponse");
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

}
