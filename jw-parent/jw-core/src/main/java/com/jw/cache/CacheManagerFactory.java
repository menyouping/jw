package com.jw.cache;

import com.jw.web.context.AppContext;

public class CacheManagerFactory {

    public static CacheManager getManager() {
        return AppContext.getBean("cacheManager");
    }

}
