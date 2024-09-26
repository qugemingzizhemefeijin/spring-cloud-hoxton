package com.atguigu.springcloud.configuration;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.service.IOperatorGetService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class LogRecordConfiguration {

    private final Splitter splitter = Splitter.on(",").trimResults();

    // IOperatorGetService 是扩展点 重写 OperatorGetServiceImpl 通过上下文获取用户的扩展
    @Bean
    public IOperatorGetService operatorGetService() {
        return () -> {
            Operator user = new Operator();
            user.setOperatorId("完美世界");
            return user;
        };
    }

    // ILogRecordService 保存 / 查询日志的例子, 使用者可以根据数据量保存到合适的存储介质上，比如保存在数据库 / 或者 ES。自己实现保存和删除就可以了
    @Bean
    public ILogRecordService logRecordService() {
        return new ILogRecordService() {

            @Override
            public void record(LogRecord logRecord) {
                log.info("【日志】log={}", logRecord);
            }

            @Override
            public List<LogRecord> queryLog(String bizNo, String type) {
                return Lists.newArrayList();
            }

            @Override
            public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
                return Lists.newArrayList();
            }
        };
    }

    // IParseFunction 自定义转换函数的接口，可以实现 IParseFunction 实现对 LogRecord 注解中使用的函数扩展
    @Bean
    public IParseFunction userParseFunction() {
        return new IParseFunction() {

            @Override
            public String functionName() {
                return "USER";
            }

            @Override
            public String apply(Object value) {
                if (StringUtils.isEmpty(value)) {
                    return null;
                }
                List<String> userIds = Lists.newArrayList(splitter.split(value.toString()));

                Map<String, String> userMap = ImmutableMap.of("1", "小橙子", "2", "小橘子", "3", "小芒果");

                StringBuilder stringBuilder = new StringBuilder();
                for (String userId : userIds) {
                    stringBuilder.append(userId);
                    if (userMap.get(userId) != null) {
                        stringBuilder.append("(").append(userMap.get(userId)).append(")");
                    }
                    stringBuilder.append(",");
                }
                return stringBuilder.toString().replaceAll(",$", "");
            }
        };
    }

}
