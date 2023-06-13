package com.atguigu.springcloud.agent;

import groovy.lang.Binding;
import groovy.lang.Script;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Utils {

    public static List<Object> checkIntercept(String className, String methodName, Map<?, ?> params) {
        try {
            Script script = MyGroovyEngine.getScript(className + "#" + methodName);
            if (script != null) {
                System.out.println("加载Groovy脚本=====");

                Binding bind = new Binding();
                for (Map.Entry<?, ?> me : params.entrySet()) {
                    bind.setVariable(String.valueOf(me.getKey()), me.getValue());
                }

                script.setBinding(bind);

                return Collections.singletonList(script.run());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private Utils() {

    }

}
