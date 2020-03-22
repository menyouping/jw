package com.jw.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.cache.ValueWrapper;
import com.jw.domain.annotation.Entity;

public class JwUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwUtils.class);

    private final static String EDITOR_FORM_NAME = "content";

    public static boolean isBindKey(String key) {
        return key.startsWith("${") && key.endsWith("}");
    }

    /**
     * ${key} -> key
     * 
     * @param key
     * @return
     */
    public static String getBindKey(String key) {
        return key.substring(2, key.length() - 1);
    }

    public static boolean isEditorField(String fieldName) {
        return EDITOR_FORM_NAME.equals(fieldName);
    }

    public static String repeat(char ch, int count) {
        if (count < 1)
            return "";
        char[] cs = new char[count];
        for (int i = 0; i < count; i++) {
            cs[i] = ch;
        }
        return String.valueOf(cs);
    }

    public static String repeat(char ch, char split, int count) {
        if (count < 1)
            return "";
        int len = count * 2 - 1;
        char[] cs = new char[len];
        for (int i = 0; i < len; i += 2) {
            cs[i] = ch;
            if (i + 1 < len) {
                cs[i + 1] = split;
            }
        }
        return String.valueOf(cs);
    }

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

    public static Object convert(String value, Class<?> targetClaze) {
        if (String.class.equals(targetClaze)) {
            return value;
        }
        if (Integer.TYPE.equals(targetClaze) || Integer.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Integer.valueOf(value);
            }
        }
        if (Long.TYPE.equals(targetClaze) || Long.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Long.valueOf(value);
            }
        }
        if (Double.TYPE.equals(targetClaze) || Double.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Double.valueOf(value);
            }
        }
        if (Float.TYPE.equals(targetClaze) || Float.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Float.valueOf(value);
            }
        }
        if (Boolean.TYPE.equals(targetClaze) || Boolean.class.equals(targetClaze)) {
            if (value == null) {
                return false;
            } else {
                if ("y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "1".equals(value))
                    return true;
                if ("n".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "0".equals(value))
                    return true;
                return Boolean.valueOf(value);
            }
        }
        return null;
    }

    public static <A extends Annotation> List<Method> findMethods(Class<?> claze, Class<A> annoClaze) {
        Method[] methods = claze.getDeclaredMethods();
        if (CollectionUtils.isEmpty(methods))
            return null;
        List<Method> list = CollectionUtils.newLinkedList();
        for (Method method : methods) {
            if (JwUtils.isAnnotated(method, annoClaze)) {
                list.add(method);
            }
        }
        return list;
    }

    public static void runMethod(Method md, Object obj, Object... args) {
        try {
            md.setAccessible(true);
            md.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("Error raised when run method " + md, e);
        }
    }

    public static <A extends Annotation> void runMethodWithAnnotation(Class<?> claze, Object entity,
            Class<A> annoClaze) {
        if (JwUtils.isAnnotated(claze, Entity.class)) {
            List<Method> mds = JwUtils.findMethods(claze, annoClaze);
            if (!CollectionUtils.isEmpty(mds)) {
                for (Method md : mds) {
                    JwUtils.runMethod(md, entity);
                }
            }
        }
    }

    public static Object callMethod(Method md, Object obj, Object... args) {
        try {
            md.setAccessible(true);
            return md.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("Error raised when call method " + md, e);
        }
        return null;
    }

    public static boolean isValid(ValueWrapper wrapper) {
        return wrapper.getValidTime() == 0 || wrapper.getValidTime() > System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(JwUtils.repeat('?', 2));
        System.out.println(JwUtils.repeat('?', ',', 2));
    }
}
