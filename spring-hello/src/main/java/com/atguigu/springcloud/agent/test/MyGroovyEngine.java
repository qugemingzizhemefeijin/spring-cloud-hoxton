package com.atguigu.springcloud.agent.test;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyGroovyEngine {

    private static final Map<String, String> SCRIPT_TEXT_MAP = new ConcurrentHashMap<>();

    static {
        String ss = "{\"orderId\":\"12345\",\"goodsName\":\"ABC\"}";
        SCRIPT_TEXT_MAP.put("", "import com.atguigu.springcloud.agent.test.Order;import com.alibaba.fastjson.JSONObject;Order order = JSONObject.parseObject('"+ss+"', Order.class);return order;");
    }

    private static final GroovyShell SHELL = new GroovyShell();

    private static final Map<String, Script> SCRIPT_MAP = new ConcurrentHashMap<>(); //脚本缓存

    public static Object run() {
        Binding bind = new Binding();
        bind.setVariable("name", "zhangsan");
        bind.setVariable("age", "25");

        Script script = getScript("");
        script.setBinding(bind);

        return script.run();
    }

    private static Script getScript(String key) {
        Script script = SCRIPT_MAP.get(key);
        if (script != null) {
            return script;
        }

        synchronized (MyGroovyEngine.class) {
            script = SCRIPT_MAP.get(key);
            if (script != null) {
                return script;
            }

            script = SHELL.parse(SCRIPT_TEXT_MAP.get(key));
            SCRIPT_MAP.put(key, script);
        }

        return script;
    }

    public static void main(String[] args) {
        Object ret = MyGroovyEngine.run();
        System.out.println(ret);
    }

}
