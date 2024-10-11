package com.atguigu.springcloud.other.desensitization.logback;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Configuration(value = "maskConfig")
public class MaskConfig implements Serializable {

    private static final long serialVersionUID = -746315412160267803L;
    // 是否开启脱敏
    @Value("${log.mask.enable:true}")
    private Boolean enable;
    // 日志路径
    @Value("${log.mask.path:com.platform}")
    private String path;
    // 关键字规则
    @Value("${log.mask.keyword:true}")
    private Boolean keyword = true;
    // 正则表达式
    @Value("${log.mask.regrex:true}")
    private Boolean regrex = true;
    // 关键字脱敏
    @Value("#{${log.mask.keywordMap}}")
    private Map<String, List<String>> keywordMap;
    // 正则表达规则
    @Value("#{${log.mask.regexMap}}")
    private Map<String, String> regexMap;

}
