package com.atguigu.springcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomAttributeVO {

    private Long businessLineId;

    private Long attributeId;

    @Override
    public String toString() {
        return "CustomAttributeVO{" +
                "businessLineId=" + businessLineId +
                ", attributeId=" + attributeId +
                '}';
    }
}
