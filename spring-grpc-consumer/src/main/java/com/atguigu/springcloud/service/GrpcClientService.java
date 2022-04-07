package com.atguigu.springcloud.service;

import com.atguigu.springcloud.grpc_api.Grpc;
import com.atguigu.springcloud.grpc_api.GrpcServiceGrpc;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrpcClientService {

    @GrpcClient("local-grpc-server")
    private GrpcServiceGrpc.GrpcServiceBlockingStub simpleBlockStub;

    public String sendMessage(final String str) {
        //封装请求参数
        Grpc.UnaryRequest request = Grpc.UnaryRequest.newBuilder()
                .setServiceName("GrpcServiceRequest")
                .setMethodName("sendUnaryRequest")
                .setData(ByteString.copyFrom(str.getBytes()))
                .build();
        //客户端存根节点调用grpc服务接口，传递请求参数
        Grpc.UnaryResponse response = simpleBlockStub.sendUnaryRequest(request);
        log.info("client, serviceName:" + response.getServiceName() + "; methodName:" + response.getMethodName());

        return response.getMethodName();
    }

}
