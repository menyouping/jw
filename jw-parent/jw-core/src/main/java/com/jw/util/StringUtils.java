package com.jw.util;

public class StringUtils {

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
        if (StringUtils.isEmpty(str))
            return str;

        char ch = str.charAt(0);
        if (ch < 'a' || ch > 'z')
            return str;

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
        if (StringUtils.isEmpty(str))
            return str;

        char ch = str.charAt(0);
        if (ch < 'A' || ch > 'Z')
            return str;

        char[] cs = str.toCharArray();
        cs[0] = Character.toLowerCase(ch);
        return String.valueOf(cs);
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.toColumnName("cargoType"));
        System.out.println(StringUtils.toFieldName("cargo_type"));
        System.out.println(StringUtils.upperFirst("cargoType"));
        System.out.println(StringUtils.lowerFirst("CargoType"));
    }
}
