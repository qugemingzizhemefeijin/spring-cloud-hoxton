package com.atguigu.springcloud.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private String title = "我的标题";

    /**
     * 服务文件介绍
     */
    private String desc = "描述信息";

    /**
     * 网址
     */
    private String url = "https://www.baidu.com/";

    /**
     * 版本
     */
    private String version = "v1.0";

}
