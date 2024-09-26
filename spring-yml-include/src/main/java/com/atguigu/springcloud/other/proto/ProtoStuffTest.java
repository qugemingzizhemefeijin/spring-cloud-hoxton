package com.atguigu.springcloud.other.proto;

import com.atguigu.springcloud.utils.JsonUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Collections;

// 支持普通java bean 序列化成proto对象
public class ProtoStuffTest {

    static RuntimeSchema<UbixBidRequest> bidRequestSchema = RuntimeSchema.createFrom(UbixBidRequest.class);

    public static void main(String[] args) {
        UbixBidRequest request = new UbixBidRequest();
        request.setRequestId("12311233abc");
        request.setApiVersion("1.0.2");

        UbixUser user = new UbixUser();
        user.setAge(1);
        user.setGender(2);
        user.setTags(Collections.singletonList("ABC"));
        request.setUser(user);

        // 序列化
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] serializedData = ProtostuffIOUtil.toByteArray(request, bidRequestSchema, buffer);

        // 反序列化
        UbixBidRequest deserializedPerson = new UbixBidRequest();
        ProtostuffIOUtil.mergeFrom(serializedData, deserializedPerson, bidRequestSchema);

        System.out.println(JsonUtil.toJson(deserializedPerson));
    }

}
