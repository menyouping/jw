package com.jw.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    public static <T> T first(Collection<T> c) {
        return isEmpty(c) ? null : c.iterator().next();
    }

    public static <T> T first(T[] arr) {
        return isEmpty(arr) ? null : arr[0];
    }

    public static <T> int count(Collection<T> c) {
        return isEmpty(c) ? 0 : c.size();
    }

    public static <T> int count(T[] arr) {
        return isEmpty(arr) ? 0 : arr.length;
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
    
    public static void main(String[] args) {
        System.out.println(CollectionUtils.join(Arrays.asList(1, 2, 3)));
        System.out.println(CollectionUtils.join(Arrays.asList(1, 2, 3), "-"));
    }
}
