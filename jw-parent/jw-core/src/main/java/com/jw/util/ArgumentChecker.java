package com.jw.util;

import java.util.Collection;

public class ArgumentChecker {

    public static void notNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("参数不能为Null");
        }
    }

    public static void notEmpty(String arg) {
        if (StringUtils.isEmpty(arg)) {
            throw new IllegalArgumentException("字符串不能为空");
        }
    }

    public static <T> void notEmpty(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException("集合不能为空");
        }
    }

    public static void notTrue(boolean condition) {
        if (condition) {
            throw new IllegalArgumentException("表达式不能为True");
        }
    }
}
