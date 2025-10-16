package com.atguigu.springcloud.other.bytekit;

import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.utils.AgentUtils;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ASM学习 https://www.bilibili.com/opus/651204577825128456
// 在使用asm包进行动态类加载的时候的打包问题 https://www.cnblogs.com/interflow/p/18518340
// Java安全基础之字节码操作框架ASM学习 https://xz.aliyun.com/news/12777
// ASM 库的介绍和使用 https://www.lwohvye.com/2021/12/27/asm-%e5%ba%93%e7%9a%84%e4%bb%8b%e7%bb%8d%e5%92%8c%e4%bd%bf%e7%94%a8/
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
