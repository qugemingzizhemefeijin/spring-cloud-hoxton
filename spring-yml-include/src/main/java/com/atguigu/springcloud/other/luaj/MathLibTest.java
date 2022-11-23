package com.atguigu.springcloud.other.luaj;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

@Slf4j
public class MathLibTest {

    private static LuaValue j2se = JsePlatform.standardGlobals().get("math");

    public static void main(String[] args) {
        tryMathOp( "abs", 23.45 );
        tryMathOp( "abs", -23.45 );
    }

    private static void tryMathOp(String op, double x) {
        try {
            double expected = j2se.get(op).call(LuaValue.valueOf(x)).todouble();

            log.info("expected = {}", expected);
        } catch (LuaError lee) {
            log.error(lee.getMessage(), lee);
        }
    }

    private static void tryMathOp(String op, double a, double b) {
        try {
            double expected = j2se.get(op).call(LuaValue.valueOf(a), LuaValue.valueOf(b)).todouble();

            log.info("expected = {}", expected);
        } catch (LuaError lee) {
            log.error(lee.getMessage(), lee);
        }
    }

}
