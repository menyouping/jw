package com.jw.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwUtils {

    private final static String EDITOR_FORM_NAME = "content";
    
    public static boolean isBindKey(String key) {
        return key.startsWith("${") && key.endsWith("}");
    }
    /**
     * ${key} -> key
     * @param key
     * @return
     */
    public static String getBindKey(String key) {
        return key.substring(2, key.length() -1);
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

    public static <T, V> Map<T, V> newHashMap() {
        return new HashMap<T, V>();
    }

    public static <T, V> Map<T, V> newHashMap(int len) {
        return new HashMap<T, V>((int) (len / 0.75) + 1);
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<T>();
    }

    public static <T> List<T> newArrayList(int len) {
        return new ArrayList<T>(len);
    }

    public static <T> String join(Collection<T> c) {
        return join(c, ",");
    }

    public static <T> String join(Collection<T> c, String split) {
        if (c == null || c.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (T t : c) {
            if (flag) {
                sb.append(split);
            } else {
                flag = true;
            }
            sb.append(String.valueOf(t));
        }
        return sb.toString();
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

    public static void main(String[] args) {
        System.out.println(JwUtils.repeat('?', 2));
        System.out.println(JwUtils.repeat('?', ',', 2));
        System.out.println(JwUtils.join(Arrays.asList(1, 2, 3)));
        System.out.println(JwUtils.join(Arrays.asList(1, 2, 3), "-"));
    }
}
