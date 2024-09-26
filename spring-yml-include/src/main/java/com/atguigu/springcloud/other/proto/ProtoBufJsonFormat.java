package com.atguigu.springcloud.other.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ProtoBufJsonFormat {

    private static final JsonFormat.Parser PARSER = JsonFormat.parser();
    private static final JsonFormat.Printer PRINTER = JsonFormat.printer().omittingInsignificantWhitespace();

    private ProtoBufJsonFormat() {
        throw new AssertionError("No Instance.");
    }

    public static String toJsonString(MessageOrBuilder message) {
        try {
            return PRINTER.print(message);
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T toJavaObject(String json, Message.Builder builder) {
        try {
            PARSER.merge(json, builder);

            return (T)builder.build();
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
