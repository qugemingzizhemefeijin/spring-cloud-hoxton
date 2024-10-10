package com.atguigu.springcloud.other.reflections;

import com.atguigu.springcloud.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class ReflectionsTest {

    public static void main(String[] args) {
        Set<Method> methods = scanAnnotationMethod("com.atguigu.springcloud.other.reflections", null, Job.class);
        if (CollectionUtils.isEmpty(methods)) {
            System.out.println("no match job method");
            return;
        }

        for (Method m : methods) {
            Job job = AnnotationUtils.getAnnotation(m, Job.class);

            Class<?>[] paramTypes = m.getParameterTypes();
            String[] rawNames = getRawNames(m);
            Class<?> returnType = m.getReturnType();
            ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(m);

            System.out.println("============================================");
            System.out.println("className = " + m.getDeclaringClass().getName() + ", methodName = " + m.getName() + ", returnName = " + returnType.getName() + ", jobName = " + job.jobName());
            if (paramTypes != null) {
                System.out.println("==================PARAMETERS================");
                for (int i = 0; i < paramTypes.length; i++) {
                    System.out.println("arg" + i + ",name=" + parameterNames[i] + ",type=" + paramTypes[i].getName() + ",raw=" + rawNames[i]);
                }
            }
            System.out.println("============================================");
        }
    }

    private static Set<Method> scanAnnotationMethod(String packagePrefix, Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation) {
        if (classAnnotation == null) {
            return new Reflections(packagePrefix, Scanners.MethodsAnnotated).getMethodsAnnotatedWith(methodAnnotation);
        }

        ConfigurationBuilder conf = new ConfigurationBuilder();
        if (StringUtils.isNotBlank(packagePrefix)) {
            conf.setUrls(ClasspathHelper.forPackage(packagePrefix));
        }

        conf.setScanners(Scanners.MethodsAnnotated, Scanners.TypesAnnotated);
        Reflections reflections = new Reflections(conf);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(classAnnotation);

        Set<Method> methods = Sets.newHashSet();
        for (Class<?> clazz : classes) {
            Reflections ref = new Reflections(clazz.getName(), Scanners.MethodsAnnotated);
            Set<Method> targetMethods = ref.getMethodsAnnotatedWith(methodAnnotation);
            methods.addAll(targetMethods);
        }

        return methods;
    }

    private static String[] getRawNames(Method method) {
        Type[] paramTypeList = method.getGenericParameterTypes();
        List<String> rawNameList = Lists.newArrayListWithCapacity(paramTypeList.length);

        for (Type paramType : paramTypeList) {
            String rawName = paramType == null ? null : paramType.toString();
            rawNameList.add(rawName);
        }

        return rawNameList.toArray(new String[0]);
    }

}
