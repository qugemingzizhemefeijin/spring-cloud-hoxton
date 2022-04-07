package com.atguigu.springcloud.service;

import com.atguigu.springcloud.grpc_api.Grpc;
import com.atguigu.springcloud.grpc_api.GrpcServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.access.annotation.Secured;

@Slf4j
@GrpcService
public class MyGrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    //UnaryRequest 客户端请求参数，
    //StreamObserver<UnaryResponse> 返回给客户端的封装参数
    @Override
    @Secured("ROLE_GREET")
    public void sendUnaryRequest(Grpc.UnaryRequest request, StreamObserver<Grpc.UnaryResponse> responseObserver) {
        ByteString message = request.getData();
        log.info("server, serviceName:" + request.getServiceName()
                + "; methodName:" + request.getMethodName() + "; datas:" + new String(message.toByteArray()));
        Grpc.UnaryResponse.Builder builder = Grpc.UnaryResponse.newBuilder();
        builder.setServiceName("GrpcServiceResponse").setMethodName("sendUnaryResponse" + System.currentTimeMillis());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

}
