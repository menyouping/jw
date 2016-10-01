package com.jw.cache;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.util.ConfigUtils;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;

public class JwCacheManager extends AbstractCacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwCacheManager.class);

    private static Properties config = ConfigUtils.getProperties("cache");

    public static final String DEFAULT_CACHE = "defaultCache";

    private Set<Cache> caches = new HashSet<>();

    public JwCacheManager() {
        registerCaches();
        afterPropertiesSet();
    }

    private void registerCaches() {
        Set<String> props = config.stringPropertyNames();
        for (String prop : props) {
            if (!prop.endsWith(".claze"))
                continue;
            String cacheName = prop.replace(".claze", "");
            String clazeName = getProperty(cacheName, "claze");

            try {
                Class<?> claze = Class.forName(clazeName);
                Cache cache = (Cache) claze.newInstance();
                Properties cacheProperties = StringUtils.isEmpty(cacheName) ? config
                        : ConfigUtils.getProperties(config, cacheName);
                cacheProperties.put("name", cacheName);

                for (String key : cacheProperties.stringPropertyNames()) {
                    if ("claze".equals(key))
                        continue;
                    Field field = claze.getDeclaredField(key);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(cache, JwUtils.convert(cacheProperties.getProperty(key), field.getType()));
                    } else {
                        LOGGER.error("The config cache." + cacheName + (StringUtils.isEmpty(cacheName) ? "" : ".") + key
                                + " is no correspond field in cache claze.");
                    }
                }
                if (StringUtils.isEmpty(cacheName)) {
                    cacheName = DEFAULT_CACHE;
                }
                caches.add(cache);
                LOGGER.info("Successfully to create the cache {}", cacheName);
            } catch (Exception e) {
                LOGGER.error("Error raised when initialize the cache " + cacheName, e);
            }
        }
    }

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = new HashSet<>(caches);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    private static String getProperty(String dbName, String key) {
        if (dbName.isEmpty()) {
            return config.getProperty(key);
        }
        return config.getProperty(dbName + "." + key);
    }

}
