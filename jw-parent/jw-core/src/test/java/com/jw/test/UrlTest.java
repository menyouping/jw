package com.jw.test;

import com.jw.util.ConfigUtils;
import com.jw.util.StringUtils;

public class UrlTest {
    public static void main(String[] args) {
        ConfigUtils.put("jw.abc", "jw");
        ConfigUtils.put("jw.admin", "admin");
        System.out.println(StringUtils.replaceConfig("/hello/${jw.abc}"));
        System.out.println(StringUtils.replaceConfig("/${jw.admin}/${jw.abc}"));
    }
}
