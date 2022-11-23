package com.atguigu.springcloud.other.aviator;

import com.googlecode.aviator.*;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.io.IOException;
import java.util.Map;

// https://www.yuque.com/boyan-avfmj/aviatorscript/cpow90 介绍说明
public class AviatorTest {

    public static void main(String[] args) throws IOException {
        // jieishiRun();
        // compileRun();

        // 当官方函数不满足的情况下，可以自定义函数
        //注册函数
//        AviatorEvaluator.addFunction(new AddFunction());
//        System.out.println(AviatorEvaluator.execute("add(1, 2)"));           // 3.0
//        System.out.println(AviatorEvaluator.execute("add(add(1, 2), 100)")); // 103.0

        Expression exp = AviatorEvaluator.getInstance().compileScript("aviator/hello.av"); // Run the exprssion
        exp.execute();
    }

    static class AddFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env,
                                  AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            return new AviatorDouble(left.doubleValue() + right.doubleValue());
        }
        public String getName() {
            return "add";
        }
    }

    private static void compileRun() {
        // AviatorEvaluator.EVAL，默认值，以运行时的性能优先，编译会花费更多时间做优化，目前会做一些常量折叠、公共变量提取的优化。适合长期运行的表达式。
        // AviatorEvaluator.COMPILE，以编译的性能优先，不会做任何编译优化，牺牲一定的运行性能，适合需要频繁编译表达式的场景。
        AviatorEvaluator.getInstance().setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE);

        //  AviatorScript 中字符串可以是双引号括起来，也可以是单引号括起来，作为字面量表达就省去了转义的麻烦。
        Expression script = AviatorEvaluator.getInstance().compile("println('Hello, AviatorScript!');");
        script.execute();

        // AviatorEvaluator.compile 是 AviatorEvaluator.getInstance().compile 的等价方法。

        String expression = "a-(b-c) > 100";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        // Execute with injected variables.
        Boolean result = (Boolean) compiledExp.execute(compiledExp.newEnv("a", 100.3, "b", 45, "c", -199.100));
        System.out.println(result);
    }

    private static void jieishiRun() {
        // 创建解释器
        AviatorEvaluatorInstance engine = AviatorEvaluator.newInstance(EvalMode.INTERPRETER);
        // 打开跟踪执行
        engine.setOption(Options.TRACE_EVAL, true);

        Expression exp = engine.compile("score > 80 ? 'good' : 'bad'");
        System.out.println(exp.execute(exp.newEnv("score", 100)));
        System.out.println(exp.execute(exp.newEnv("score", 50)));
    }

}
