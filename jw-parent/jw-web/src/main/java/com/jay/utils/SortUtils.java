package com.jay.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortUtils {

    private static final Comparator<String> ASC = new Comparator<String>() {

        public int compare(String left, String right) {

            return left.compareTo(right);
        }
    };

    /**
     * 为Map按key进行升序排序 排序规则:按照 ASSIC 编码升序
     *
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map<String, Object> asc(Map<String, Object> map) {

        if (map == null || map.isEmpty()) {
            return null;
        }

        if (map instanceof SortedMap && ((SortedMap) map).comparator() == ASC) {
            return map;
        }

        Set<Entry<String, Object>> enties = map.entrySet();
        Object value = null;
        for (Map.Entry<String, Object> entry : enties) {
            value = entry.getValue();
            if (value instanceof List) {
                entry.setValue(asc((List<Object>) value));
            } else if (value instanceof Map) {
                entry.setValue(asc((Map<String, Object>) value));
            }
            // 判断是否字符串,若是过滤特殊字符
            else if (value instanceof String) {
                entry.setValue(filterString(value.toString()));
            }
        }

        Map<String, Object> sortedMap = new TreeMap<String, Object>(ASC);
        sortedMap.putAll(map);

        return sortedMap;
    }

    /**
     * 过滤0X00A0
     *
     * @param str 需要过滤的字符串
     * @return String
     */
    public static String filterString(String str) {

        if (str == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == 0X00A0) {
                continue;
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    /**
     * 对List中的递归排序
     *
     */
    @SuppressWarnings("unchecked")
    public static List<Object> asc(List<Object> list) {

        if (list == null || list.isEmpty()) {
            return list;
        }

        int len = list.size();
        Object item = null;
        for (int i = 0; i < len; i++) {
            item = list.get(i);
            if (item instanceof List) {
                list.set(i, asc((List<Object>) item));
            } else if (item instanceof Map) {
                list.set(i, asc((Map<String, Object>) item));
            }
        }
        return list;
    }
}
