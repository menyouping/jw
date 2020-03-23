package com.jw.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * ${xxx}
     */
    public static final Pattern PATTERN_CONFIG = Pattern.compile("(\\$\\{([^}]+)\\})");

    /**
     * {xxx}
     */
    public static final Pattern PATTERN_PATH_VARIABLE = Pattern.compile("(\\{([^}]+)\\})");

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 数据库字段名 e.g. cargoType => cargo_type
     * 
     * @param str
     * @return
     */
    public static String toColumnName(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        StringBuilder sb = new StringBuilder(str.length() + 2);
        sb.append(Character.toLowerCase(str.charAt(0)));
        int len = str.length();
        for (int i = 1; i < len; i++) {
            char ch = str.charAt(i);
            if (ch < 'A' || ch > 'Z') {
                sb.append(ch);
            } else {
                sb.append('_');
                sb.append(Character.toLowerCase(ch));
            }
        }
        return sb.toString();
    }

    /**
     * Bean字段名 e.g. cargo_type => cargoType
     * 
     * @param str
     * @return
     */
    public static String toFieldName(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        StringBuilder sb = new StringBuilder(str.length());

        char ch;
        int len = str.length();
        if (str.charAt(len - 1) == '_') {
            len--;
        }
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == '_') {
                sb.append(Character.toUpperCase(str.charAt(++i)));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     * 
     * @param str
     * @return
     */
    public static String upperFirst(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        char ch = str.charAt(0);
        if (ch < 'a' || ch > 'z') {
            return str;
        }

        char[] cs = str.toCharArray();
        cs[0] = Character.toUpperCase(ch);
        return String.valueOf(cs);
    }

    /**
     * 首字母小写
     * 
     * @param str
     * @return
     */
    public static String lowerFirst(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        char ch = str.charAt(0);
        if (ch < 'A' || ch > 'Z') {
            return str;
        }

        char[] cs = str.toCharArray();
        cs[0] = Character.toLowerCase(ch);
        return String.valueOf(cs);
    }

    /**
     * replace the config <br>
     * e.g./hello/${jw.abc}
     * 
     * @param url
     * @return
     */
    public static String replaceConfig(String url) {
        Matcher m = PATTERN_CONFIG.matcher(url);
        if (!m.find()) {
            return url;
        }
        String key, value;
        int start = 0, offset = 0;
        StringBuilder sb = new StringBuilder(url);
        while (m.find(start)) {
            key = m.group(2);
            value = ConfigUtils.getString(key);
            sb.replace(m.start() + offset, m.end() + offset, value);
            offset += value.length() - (key.length() + 3);// 3 = "${}".length
            start = m.end();
        }
        return sb.toString();
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.toColumnName("cargoType"));
        System.out.println(StringUtils.toFieldName("cargo_type"));
        System.out.println(StringUtils.upperFirst("cargoType"));
        System.out.println(StringUtils.lowerFirst("CargoType"));
    }
}
