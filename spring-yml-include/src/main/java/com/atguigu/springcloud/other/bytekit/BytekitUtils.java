package com.atguigu.springcloud.other.bytekit;

import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.utils.AgentUtils;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Set;

public class BytekitUtils {

    public static void reTransformClass(Instrumentation instrumentation, List<InterceptorProcessor> processors,
                                        String className, Set<String> methodNames, boolean subClass) {
        Set<Class<?>> classes = SearchUtils.searchClassOnly(instrumentation, className, false);
        if (classes.isEmpty()) {
            return;
        }
        Set<Class<?>> subClasses = classes;
        if (subClass) {
            subClasses = SearchUtils.searchSubClass(instrumentation, classes);
        }
        reTransform(processors, subClasses, methodNames);
    }

    public static void reTransform(List<InterceptorProcessor> processors, Set<Class<?>> classes, Set<String> methodNames) {
        for (Class<?> cls : classes) {
            ClassNode classNode = null;
            try {
                classNode = AsmUtils.loadClass(cls);
                classNode = AsmUtils.removeJSRInstructions(classNode);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            boolean inited = false;
            for (MethodNode methodNode : classNode.methods) {
                if (methodNames == null || methodNames.contains(methodNode.name)) {
                    MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode);
                    for (InterceptorProcessor interceptor : processors) {
                        try {
                            interceptor.process(methodProcessor);
                            inited = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (!inited) {
                continue;
            }

            byte[] bytes = AsmUtils.toBytes(classNode);
            try {
                AgentUtils.reTransform(cls, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
