package com.jw.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jw.util.ConfigUtils;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ConfigUtils.init();
        AppContext.init();
        UrlMappingRegistry.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

}
