package com.jw.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static <A extends Annotation> boolean isAnnotated(Class<?> claze, Class<A> annoClaze) {
        if (claze.getAnnotation(annoClaze) != null) {
            return true;
        }

        Annotation[] annos = claze.getAnnotations();
        for (Annotation anno : annos) {
            if (anno.annotationType().getAnnotation(annoClaze) != null) {
                return true;
            }
        }
        return false;
    }

    public static <A extends Annotation> boolean isAnnotated(Field field, Class<A> annoClaze) {
        return field.getAnnotation(annoClaze) != null;
    }

    public static <A extends Annotation> boolean isAnnotated(Method method, Class<A> annoClaze) {
        return method.getAnnotation(annoClaze) != null;
    }

    public static boolean contains(Annotation[] annos, Class<? extends Annotation> annoClaze) {
        for (Annotation a : annos) {
            if (a.annotationType() == annoClaze) {
                return true;
            }
        }
        return false;
    }
}
