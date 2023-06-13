package com.atguigu.springcloud.agent;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class InterceptTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            return classfileBuffer;
        }
        String fullClassName = className.replace("/", ".");

        // 使用javassist修改字节码文件
        try {
            // 包必须符合要求
            if (!fullClassName.startsWith("com.atguigu.springcloud.agent.test")) {
                return classfileBuffer;
            }

            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(fullClassName);

            // 接口/私有，不支持
            if (cc.isInterface() || cc.isModified() || Modifier.isAbstract(cc.getModifiers())) {
                return classfileBuffer;
            }

            System.out.println("loadClassName = " + className);

            CtMethod[] methods = cc.getDeclaredMethods();

            for (CtMethod declaredMethod : methods) {
                System.out.println(className + "============>" + declaredMethod.getName());
                // 公共方法或者非静态，非抽象
                if (Modifier.isPublic(declaredMethod.getModifiers()) || !Modifier.isStatic(declaredMethod.getModifiers()) || !Modifier.isAbstract(declaredMethod.getModifiers())) {
                    System.out.println(fullClassName+"#"+declaredMethod.getName());
                    if (MyGroovyEngine.supportProxy(fullClassName, declaredMethod.getName())) {
                        String returnType = declaredMethod.getReturnType().getName();
                        System.out.println("proxy:" + declaredMethod.getName() + "():returnType:"+returnType);

                        MethodInfo methodInfo = declaredMethod.getMethodInfo();
                        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
                        int pos = Modifier.isStatic(declaredMethod.getModifiers()) ? 0 : 1;

                        StringBuilder byteCode = new StringBuilder();
                        byteCode.append("java.util.Map params = new java.util.HashMap();\n");
                        CtClass[] paramTypes = declaredMethod.getParameterTypes();
                        int count = paramTypes.length;
                        for (int i = 0; i < count; i++) {
                            String paramName = attr.variableName(i + pos);
                            String paramType = paramTypes[i].getName();
                            String paramArgs = paramName;

                            switch (paramType) {
                                case "int":
                                    paramArgs = "Integer.valueOf("+paramName+")";
                                    break;
                                case "long":
                                    paramArgs = "Long.valueOf("+paramName+")";
                                    break;
                                case "float":
                                    paramArgs = "Float.valueOf("+paramName+")";
                                    break;
                                case "double":
                                    paramArgs = "Double.valueOf("+paramName+")";
                                    break;
                                case "short":
                                    paramArgs = "Short.valueOf("+paramName+")";
                                    break;
                                case "byte":
                                    paramArgs = "Byte.valueOf("+paramName+")";
                                    break;
                                case "boolean":
                                    paramArgs = "Boolean.valueOf("+paramName+")";
                                    break;
                                case "char":
                                    paramArgs = "Character.valueOf("+paramName+")";
                                    break;
                            }

                            byteCode.append("params.put(\"").append(paramName).append("\", ").append(paramArgs).append(");\n");
                        }

                        byteCode.append("java.util.List ret = com.atguigu.springcloud.agent.Utils.checkIntercept(\"").append(fullClassName).append("\", \"").append(declaredMethod.getName()).append("\", params);\n");
                        if(!"void".equals(returnType)) {
                            byteCode.append("if (ret != null) {return ");

                            switch (returnType) {
                                case "int":
                                    byteCode.append("((Number)ret.get(0)).intValue();");
                                    break;
                                case "long":
                                    byteCode.append("((Number)ret.get(0)).longValue();");
                                    break;
                                case "float":
                                    byteCode.append("((Number)ret.get(0)).floatValue();");
                                    break;
                                case "double":
                                    byteCode.append("((Number)ret.get(0)).doubleValue();");
                                    break;
                                case "short":
                                    byteCode.append("((Number)ret.get(0)).shortValue();");
                                    break;
                                case "byte":
                                    byteCode.append("((Number)ret.get(0)).byteValue();");
                                    break;
                                case "boolean":
                                    byteCode.append("((Boolean)ret.get(0)).booleanValue();");
                                    break;
                                case "char":
                                    byteCode.append("((Character)ret.get(0)).charValue();");
                                    break;
                                default:
                                    byteCode.append("(").append(returnType).append(")ret.get(0);");
                            }
                            byteCode.append("}");
                        }

                        System.out.println("==================== byteCode ====================");
                        System.out.println(byteCode);
                        System.out.println("===================================================");

                        // 在方法体开头插入语句
                        declaredMethod.insertBefore(byteCode.toString());
                    }
                }
            }

            cc.detach();
            return cc.toBytecode();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace(System.out);
        }

        return classfileBuffer;
    }

}
