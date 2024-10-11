package com.atguigu.springcloud.other.desensitization.jackson;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveEnum sensitive;

    // jackson 序列化
    @Override
    public void serialize(String value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeString(sensitive.express.apply(value));
    }

    // 匹配脱敏处理器
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        Sensitivity annotation = property.getAnnotation(Sensitivity.class);
        if (ObjectUtil.isNotEmpty(annotation) && ObjectUtil.equal(String.class, property.getType().getRawClass())) {
            this.sensitive = annotation.strategy();
            return this;
        }
        return provider.findValueSerializer(property.getType(), property);
    }

}
