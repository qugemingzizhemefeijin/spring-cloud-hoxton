package com.atguigu.springcloud.other.proto;

import java.util.Collections;

// protoc --java_out=./ proto/*.proto
public class ProtoJsonTest {

    public static void main(String[] args) {
        UbixUserModel.UbixUser user = UbixUserModel.UbixUser.newBuilder()
                .setAge(18)
                .setUid("A1001")
                .setGender(0)
                .addAllKeywords(Collections.singletonList("Hello World"))
                .setExt("ABC")
                .addAllTags(Collections.singletonList("IT"))
                .addAllCategory(Collections.singletonList("主播"))
                .addAllInstalledApps(Collections.singletonList("kuaishou"))
                .build();

        UbixBidRequestModel.UbixBidRequest request = UbixBidRequestModel.UbixBidRequest.newBuilder()
                .setApiVersion("1.0.0")
                .setRequestId("12312313")
                .setUser(user)
                .build();


        String json = ProtoBufJsonFormat.toJsonString(request);
        System.out.println(json);


        request = ProtoBufJsonFormat.toJavaObject(json, UbixBidRequestModel.UbixBidRequest.newBuilder());

        System.out.println(ProtoBufJsonFormat.toJsonString(request));

        // Files.write(Paths.get("E:/aaa.bin"), request.toByteArray());
    }

}
