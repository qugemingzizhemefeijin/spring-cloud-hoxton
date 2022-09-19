package com.atguigu.springcloud.openfeign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cec")
public class CECOperatorProperties {

    private String sigSecret;

    private String dataSecret;

    private String dataIv;

    private String operatorID;

}
