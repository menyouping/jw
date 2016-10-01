package com.jw.aop.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JoinPoint;
import com.jw.aop.JointPointParameter;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Aspect;
import com.jw.aop.annotation.Pointcut;
import com.jw.cache.Cache;
import com.jw.cache.CacheManager;
import com.jw.cache.CacheManagerFactory;
import com.jw.cache.CacheUtils;
import com.jw.cache.annotation.CacheEvict;
import com.jw.util.JwUtils;

@Aspect
public class CacheEvictAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheEvictAspect.class);

    @Pointcut("@annotation(com.jw.cache.annotation.CacheEvict)")
    public void cachePointcut() {

    }

    @Around("cachePointcut()")
    public Object aroundCacheHold(JoinPoint jp, final JointPointParameter parameter) {
        Object result = null;
        try {
            result = parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
            CacheEvict ann = parameter.getMethod().getAnnotation(CacheEvict.class);
            String[] cacheNames = ann.value();

            if (!JwUtils.isEmpty(cacheNames)) {
                String key = CacheUtils.getCacheKey(parameter, cacheNames, ann.prefix(), ann.join());
                CacheManager manager = CacheManagerFactory.getManager();
                for (String cacheName : cacheNames) {
                    Cache cache = manager.getCache(cacheName);
                    cache.evict(key);
                }
            }
        } catch (Throwable e) {
            LOGGER.error("Error raised when invorked method " + parameter, e);
        }
        return result;
    }

}
