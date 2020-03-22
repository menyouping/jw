package com.jw.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.domain.annotation.Entity;

public class ReflectionUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <A extends Annotation> List<Method> findMethods(Class<?> claze, Class<A> annoClaze) {
        Method[] methods = claze.getDeclaredMethods();
        if (CollectionUtils.isEmpty(methods)) {
            return null;
        }
        return Arrays.asList(methods).stream()//
                .filter(method -> AnnotationUtils.isAnnotated(method, annoClaze))//
                .collect(Collectors.toList());
    }

    public static void runMethod(Method md, Object obj, Object... args) {
        try {
            md.setAccessible(true);
            md.invoke(obj, args);
        } catch (Exception e) {
            LOG.error("Error raised when run method " + md, e);
        }
    }

    public static <A extends Annotation> void runMethodWithAnnotation(Class<?> claze, Object entity,
            Class<A> annoClaze) {
        if (AnnotationUtils.isAnnotated(claze, Entity.class)) {
            List<Method> mds = findMethods(claze, annoClaze);
            if (!CollectionUtils.isEmpty(mds)) {
                for (Method md : mds) {
                    runMethod(md, entity);
                }
            }
        }
    }

    public static Object callMethod(Method md, Object obj, Object... args) {
        try {
            md.setAccessible(true);
            return md.invoke(obj, args);
        } catch (Exception e) {
            LOG.error("Error raised when call method " + md, e);
        }
        return null;
    }
}
