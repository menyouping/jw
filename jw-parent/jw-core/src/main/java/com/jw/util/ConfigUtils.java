package com.jw.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigUtils {
    private static Properties config = new Properties();

    static {
        List<File> files = FileUtils.findFiles(".properties");
        Properties prop = null;
        for (File file : files) {
            prop = new Properties();
            try {
                prop.load(new FileReader(file));
                config.putAll(prop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void init() {

    }

    public static Properties getProperties() {
        return config;
    }

    public static Properties getProperties(String nameSpace) {
        Properties p = new Properties();
        String prefix = nameSpace + ".";
        for (String key : config.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                p.put(key.substring(prefix.length()), config.get(key));
            }
        }
        return p;
    }

    public static Object put(String key, Object value) {
        return config.put(key, value);
    }

    public void putAll(Map<String, ? extends Object> map) {
        config.putAll(map);
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    public static String getString(String key) {
        if (config.get(key) == null)
            return "";
        return config.get(key).toString();
    }

    @SuppressWarnings("deprecation")
    public static String getCNString(String key) {
        if (config.get(key) == null)
            return "";
        return URLDecoder.decode(config.get(key).toString());
    }

    public static String getLower(String key) {
        if (config.get(key) == null)
            return "";
        return config.get(key).toString().toLowerCase();
    }

    public static String getUpper(String key) {
        if (config.get(key) == null)
            return "";
        return config.get(key).toString().toUpperCase();
    }

    public static Double getDouble(String key) {
        String val = getString(key);
        return val == null || val.isEmpty() ? 0d : Double.valueOf(val);
    }

    public static Integer getInt(String key) {
        String val = getString(key);
        return val == null || val.isEmpty() ? 0 : Integer.valueOf(val);
    }

    public static Long getLong(String key) {
        String val = getString(key);
        return val == null || val.isEmpty() ? 0 : Long.valueOf(val);
    }

    public static String getString(String key, String defaultValue) {
        if (config.get(key) == null || "".equals(config.get(key)))
            return defaultValue;
        return config.get(key).toString();
    }

    public static Double getDouble(String key, Double defaultValue) {
        String val = getString(key);
        return val == null || val.isEmpty() ? defaultValue : Double.valueOf(val);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        String val = getString(key);
        return val == null || val.isEmpty() ? defaultValue : Integer.valueOf(val);
    }

    public static boolean notEqual(String key, Object value) {
        Object val = config.get(key);
        return !value.toString().equals(val.toString());
    }

    public static Long getLong(String key, Long defaultValue) {
        String val = getString(key);
        return val == null || val.isEmpty() ? defaultValue : Long.valueOf(val);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String val = getString(key);
        return val == null || val.isEmpty() ? defaultValue : Boolean.valueOf(val);
    }

    public static ThreadLocal<DateFormat> dateThreadLocal = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static ThreadLocal<DateFormat> timeThreadLocal = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static Date getDate(String key) {
        String val = getString(key);
        try {
            return val == null || val.isEmpty() || "NONE".equalsIgnoreCase(val) ? null
                    : dateThreadLocal.get().parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateTime(String key) {
        String val = getString(key);
        try {
            return val == null || val.isEmpty() || "NONE".equalsIgnoreCase(val) ? null
                    : timeThreadLocal.get().parse(val);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getCustomsDate(String key, String pattern) throws ParseException {
        String val = getString(key);
        return val == null || val.isEmpty() || "NONE".equalsIgnoreCase(val) ? null
                : new SimpleDateFormat(pattern).parse(val);
    }

    public static Date getCustomsDate(String key, DateFormat format) throws ParseException {
        String val = getString(key);
        return val == null || val.isEmpty() || "NONE".equalsIgnoreCase(val) ? null : format.parse(val);
    }

    public static Date getNextDay(String key) {
        Date today = getDate(key);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }

}
