package com.jw.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jw.aop.JwProxyRegistry;
import com.jw.cache.JwCacheManager;
import com.jw.db.JwDBManager;
import com.jw.util.ConfigUtils;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ConfigUtils.init();
        JwProxyRegistry.init();
        AppContext.init();
        if (ConfigUtils.containsKey("db.manager.claze")) {
            try {
                AppContext.setBean("dbManager", Class.forName(ConfigUtils.getProperty("db.manager.claze")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            AppContext.setBean("dbManager", new JwDBManager());
        }
        if (ConfigUtils.containsKey("cache.manager.claze")) {
            try {
                AppContext.setBean("cacheManager", Class.forName(ConfigUtils.getProperty("cache.manager.claze")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            AppContext.setBean("cacheManager", new JwCacheManager());
        }
        UrlMappingRegistry.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

}
