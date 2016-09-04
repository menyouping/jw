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

public class JwUtils {

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

    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
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

    public static <T> List<T> newLinkedList() {
        return new LinkedList<T>();
    }

    public static <T> List<T> newArrayList(int len) {
        return new ArrayList<T>(len);
    }

    public static <T> String join(Collection<T> c) {
        if (c == null || c.isEmpty())
            return "";
        return join(c.toArray(), ",");
    }

    public static <T> String join(Collection<T> c, String split) {
        if (c == null || c.isEmpty())
            return "";
        return join(c.toArray(), split);
    }

    public static <T extends Object> String join(T[] a) {
        return join(a, ",");
    }

    public static <T extends Object> String join(T[] a, String split) {
        if (a == null || a.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (T t : a) {
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

    public static Object convert(Class<?> targetClaze, String value) {
        if (String.class.equals(targetClaze)) {
            return value;
        }
        if (Integer.TYPE.equals(targetClaze) || Integer.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Integer.valueOf(value);
            }
        } else if (Long.TYPE.equals(targetClaze) || Long.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Long.valueOf(value);
            }
        } else if (Double.TYPE.equals(targetClaze) || Double.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Double.valueOf(value);
            }
        } else if (Float.TYPE.equals(targetClaze) || Float.class.equals(targetClaze)) {
            if (value == null) {
                return 0;
            } else {
                return Float.valueOf(value);
            }
        } else if (Boolean.TYPE.equals(targetClaze) || Boolean.class.equals(targetClaze)) {
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

    public static void main(String[] args) {
        System.out.println(JwUtils.repeat('?', 2));
        System.out.println(JwUtils.repeat('?', ',', 2));
        System.out.println(JwUtils.join(Arrays.asList(1, 2, 3)));
        System.out.println(JwUtils.join(Arrays.asList(1, 2, 3), "-"));
    }
}
