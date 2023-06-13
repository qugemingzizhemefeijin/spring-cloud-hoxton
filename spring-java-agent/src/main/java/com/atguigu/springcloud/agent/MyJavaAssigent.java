package com.atguigu.springcloud.agent;

import javassist.ClassPool;
import javassist.NotFoundException;

import java.io.File;
import java.lang.instrument.Instrumentation;

public class MyJavaAssigent {

    private static final String APP_JAR_PATH = System.getProperty("app.jar.path","");

    public static void premain(String args, Instrumentation instrumentation) {
        initialized();

        InterceptTransformer transformer = new InterceptTransformer();
        instrumentation.addTransformer(transformer);
    }

    private static void initialized() {
        try {
            ClassPool pool = ClassPool.getDefault();

            if (APP_JAR_PATH != null && APP_JAR_PATH.length() > 0) {
                String[] appJars = APP_JAR_PATH.split(":");
                for (String appJar : appJars) {
                    // 目录
                    if (appJar.endsWith("/")) {
                        appendClassPathForDir(pool, appJar);
                    } else {
                        System.out.println("appendClass Jar = " + appJar);
                        pool.appendClassPath(appJar);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void appendClassPathForDir(ClassPool pool, String dirPath) throws NotFoundException {
        File file = new File(dirPath);
        if (!file.isDirectory()) {
            return;
        }

        File[] fs = file.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }

        for (File f : fs) {
            if (f.isFile()) {
                String pathPath = f.getPath();
                if (pathPath.endsWith(".jar")) {
                    System.out.println("appendClass Jar = " + pathPath);
                    pool.appendClassPath(pathPath);
                }
            }
        }
    }

}
