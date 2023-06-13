package com.atguigu.springcloud.agent;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyGroovyEngine {

    private static final GroovyShell SHELL = new GroovyShell();

    private static final Map<String, GroovyScript> SCRIPT_MAP = new ConcurrentHashMap<>(); //脚本缓存

    static {
        GroovyScript s = new GroovyScript();
        s.setName("com.atguigu.springcloud.agent.test.Client#getOrderDetail");
        s.setText("import com.atguigu.springcloud.agent.test.Order;import com.alibaba.fastjson.JSONObject;Order order = JSONObject.parseObject('{\"orderId\":\"12345\",\"goodsName\":\"ABC\"}', Order.class);return order;");
        s.setMd5sum(MD5.encode(s.getText()));

        GroovyScript x = new GroovyScript();
        x.setName("com.atguigu.springcloud.agent.test.Client#getOrderCount");
        x.setText("println c;return c + 100;");
        x.setMd5sum(MD5.encode(x.getText()));

        SCRIPT_MAP.put(s.getName(), s);
        SCRIPT_MAP.put(x.getName(), x);
    }

    public static Script getScript(String key) {
        GroovyScript groovyScript = SCRIPT_MAP.get(key);
        if (groovyScript != null) {
            Script script = groovyScript.getScript();
            if (script != null) {
                return script;
            }
            synchronized (MyGroovyEngine.class) {
                script = groovyScript.getScript();
                if (script != null) {
                    return script;
                }

                script = SHELL.parse(groovyScript.getText());
                groovyScript.setScript(script);
                return script;
            }
        }

        return null;
    }

    public static boolean supportProxy(String className, String methodName) {
        return SCRIPT_MAP.containsKey(className + "#" + methodName);
    }

}
