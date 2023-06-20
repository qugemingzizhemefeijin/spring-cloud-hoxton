package com.atguigu.springcloud.other.bytekit;

import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.utils.AgentUtils;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BytekitTest {

    public static void main(String[] args) {
        Class<?> ss = Statement.class;
        // 使用之前需要让类先加载一下
        init();

        System.out.println("===");

        Statement statement = new Statement();
        Result result = statement.execute("select * from dual");
        System.out.println(result.getUpdateCount());
        System.out.println(result.getBytesSize());
        System.out.println(result.getResults());
    }

    public static void init() {
        Instrumentation instrumentation = AgentUtils.install();
        DefaultInterceptorClassParser interceptorClassParser = new DefaultInterceptorClassParser();
        List<InterceptorProcessor> processors = interceptorClassParser.parse(MysqlStatementQueryInterceptor.class);

        String classPattern = "com.atguigu.springcloud.other.bytekit.Statement";
        Set<String> methodNames = new HashSet<>();
        methodNames.add("executeQuery");
        methodNames.add("execute");
        BytekitUtils.reTransformClass(instrumentation, processors, classPattern, methodNames, true);
    }

}
