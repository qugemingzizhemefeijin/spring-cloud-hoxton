package com.atguigu.springcloud.scriptengine;

import lombok.extern.slf4j.Slf4j;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

// JDK8的默认JS引擎只有有限支持ES6语法

// 引擎名称：nashorn
// 这是 JDK 8 默认提供的 JavaScript 引擎，用于执行 JavaScript 代码。Nashorn 是对 Rhino 引擎的替代，提供了更好的性能和更广泛的 ECMAScript 5.1 支持，但对 ES6 支持有限。

// 引擎名称：groovy
// Groovy 是一种动态语言，兼容 Java。你需要单独安装 Groovy 库才能使用此引擎。

// 引擎名称：python
// Jython 是 Python 的 Java 实现。如果安装了 Jython，你可以使用这个引擎来执行 Python 代码。

// 引擎名称：ruby
// JRuby 是 Ruby 的 Java 实现。如果安装了 JRuby，你可以使用这个引擎来执行 Ruby 代码。

// 引擎名称：js
// Rhino 是早期的 JavaScript 引擎，由 Mozilla 开发。如果 Rhino 被添加到类路径中，你可以使用这个引擎。
@Slf4j
public class ScriptEngineFromFile {

    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        try (FileReader reader = new FileReader(ScriptEngineFromFile.class.getResource("/js/calc.js").getPath())) {
            engine.eval(reader);
            Invocable invEngine = (Invocable) engine;

            log.info(invEngine.invokeFunction("getURL", "https://www.baidu.com").toString().replace(" ", "%20"));
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
