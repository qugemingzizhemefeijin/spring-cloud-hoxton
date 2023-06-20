package com.atguigu.springcloud.other.bytekit;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.AtEnter;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class MysqlStatementQueryInterceptor {

    private static ThreadLocal<Long> HOLDER = new ThreadLocal<>();
    private static int THRESHOLD_COUNT = 100;
    private static int THRESHOLD_SIZE = 1 * 1024;
    private final static int THRESHOLD_ELAPSED = 10 * 1000;

    @AtEnter(inline = false)
    public static void atEnter() {
        HOLDER.set(System.currentTimeMillis());
    }

    @AtExit(inline = false)
    public static void atExit(@Binding.This Object target,
                              @Binding.Args Object[] args,
                              @Binding.MethodName String methodName,
                              @Binding.Return Object ret) {
        try {
            doAtExit(ret, args, methodName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void doAtExit(Object target, Object[] args, String methodName) throws SQLException, IllegalAccessException, InvocationTargetException {
        Field resultsField = field(target.getClass(), "results");
        resultsField.setAccessible(true);
        Object obj = resultsField.get(target);

        Method getUpdateCount = method(target.getClass(), "getUpdateCount");
        getUpdateCount.setAccessible(true);
        long updateCount = (long) getUpdateCount.invoke(target);

        Method getBytesSize = method(target.getClass(), "getBytesSize");
        getBytesSize.setAccessible(true);
        int byteSize = (int) getBytesSize.invoke(target);

        long elapsed = System.currentTimeMillis() - HOLDER.get();
        if (updateCount > THRESHOLD_COUNT || byteSize > THRESHOLD_SIZE || elapsed > THRESHOLD_ELAPSED) {
            String sql = (args.length >= 1) ? (String) args[0] : "";
            Method asSql = method(target.getClass(), "asSql");
            if (asSql != null) {
                asSql.setAccessible(true);
                sql = (String) asSql.invoke(target);
            }

            String sm = target.getClass().getName() + "." + methodName +
                    "," + sql +
                    "," + byteSize + " bytes" +
                    ",amount " + updateCount +
                    ",elapsed " + elapsed + " ms";

            RuntimeException e = new RuntimeException(sm);
            e.setStackTrace(Thread.currentThread().getStackTrace());
            e.printStackTrace();
        }
    }

    private static Field field(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            return field(clazz.getSuperclass(), fieldName);
        }
    }

    private static Method method(Class<?> clazz, String methodName) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return method(clazz.getSuperclass(), methodName);
        }
    }

}
