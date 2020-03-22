package com.jw.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置工具类
 * 
 * @author jay
 *
 */
public class ConfigUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtils.class);

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final long ONE_DAY = 24 * 3600 * 1000l;

    private static final Properties CONFIG = new Properties();

    public static void init() {
        File[] files = FileUtils.findFiles(".properties");
        if (files == null) {
            return;
        }
        Properties prop = null;
        for (File file : files) {
            prop = new Properties();
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.info("加载配置:{}", file.getName());
                }
                prop.load(new FileReader(file));
                CONFIG.putAll(prop);
            } catch (IOException e) {
                LOG.error(String.format("加载配置文件%s失败", file.getPath()), e);
            }
        }
    }

    public static Properties getProperties() {
        return CONFIG;
    }

    public static boolean containsKey(String key) {
        return CONFIG.containsKey(key);
    }

    public static Properties getProperties(String namespace) {
        return getProperties(CONFIG, namespace);
    }

    public static Properties getProperties(Properties properties, String namespace) {
        Properties p = new Properties();
        String prefix = namespace + ".";
        properties.entrySet().stream()//
                .filter(e -> ((String) e.getKey()).startsWith(prefix))// 过滤非指定namespace的key
                .forEach(e -> p.put(((String) e.getKey()).substring(prefix.length()), e.getValue()));
        return p;
    }

    public static Object put(String key, Object value) {
        return CONFIG.put(key, value);
    }

    public void putAll(Map<String, ? extends Object> map) {
        CONFIG.putAll(map);
    }

    public static String getProperty(String key) {
        return CONFIG.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return CONFIG.getProperty(key, defaultValue);
    }

    public static String getString(String key) {
        if (CONFIG.get(key) == null) {
            return "";
        }
        return CONFIG.get(key).toString();
    }

    @SuppressWarnings("deprecation")
    public static String getCNString(String key) {
        if (CONFIG.get(key) == null) {
            return "";
        }
        return URLDecoder.decode(CONFIG.get(key).toString());
    }

    public static String getLower(String key) {
        if (CONFIG.get(key) == null) {
            return "";
        }
        return CONFIG.get(key).toString().toLowerCase();
    }

    public static String getUpper(String key) {
        if (CONFIG.get(key) == null) {
            return "";
        }
        return CONFIG.get(key).toString().toUpperCase();
    }

    public static Double getDouble(String key) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? 0d : Double.valueOf(val);
    }

    public static Integer getInt(String key) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? 0 : Integer.valueOf(val);
    }

    public static Long getLong(String key) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? 0 : Long.valueOf(val);
    }

    public static String getString(String key, String defaultValue) {
        Object val = CONFIG.getOrDefault(key, defaultValue);
        return val == null ? defaultValue : val.toString();
    }

    public static Double getDouble(String key, Double defaultValue) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? defaultValue : Double.valueOf(val);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? defaultValue : Integer.valueOf(val);
    }

    public static boolean notEqual(String key, Object value) {
        Object val = CONFIG.get(key);
        return !value.toString().equals(val.toString());
    }

    public static Long getLong(String key, Long defaultValue) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? defaultValue : Long.valueOf(val);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String val = getString(key);
        return StringUtils.isEmpty(val) ? defaultValue : Boolean.valueOf(val);
    }

    public static ThreadLocal<DateFormat> dateThreadLocal = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_PATTERN);
        }
    };

    public static ThreadLocal<DateFormat> timeThreadLocal = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_TIME_PATTERN);
        }
    };

    public static Date getDate(String key) {
        return getCustomsDate(key, dateThreadLocal.get());
    }

    public static Date getDateTime(String key) {
        return getCustomsDate(key, timeThreadLocal.get());
    }

    public static Date getCustomsDate(String key, String pattern) {
        return getCustomsDate(key, new SimpleDateFormat(pattern));
    }

    public static Date getCustomsDate(String key, DateFormat format) {
        String val = getString(key);
        if (StringUtils.isEmpty(val) || "NONE".equalsIgnoreCase(val)) {
            return null;
        }
        try {
            return format.parse(val);
        } catch (ParseException e) {
            LOG.error(String.format("日期/时间配置获取失败,key:%s", key), e);
            return null;
        }
    }

    public static Date getNextDay(String key) {
        Date today = getDate(key);
        return new Date(today.getTime() + ONE_DAY);
    }

}
