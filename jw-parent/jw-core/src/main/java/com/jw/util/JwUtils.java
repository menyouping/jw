package com.jw.util;

import com.jw.cache.ValueWrapper;

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

    public static boolean isValid(ValueWrapper wrapper) {
        return wrapper.getValidTime() == 0 || wrapper.getValidTime() > System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(JwUtils.repeat('?', 2));
        System.out.println(JwUtils.repeat('?', ',', 2));
    }
}
