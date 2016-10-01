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
import com.jw.cache.JwValueWrapper;
import com.jw.cache.ValueWrapper;
import com.jw.cache.annotation.Cacheable;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;

@Aspect
public class CacheableAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheableAspect.class);

    @Pointcut("@annotation(com.jw.cache.annotation.Cacheable)")
    public void cachePointcut() {

    }

    @Around("cachePointcut()")
    public Object aroundCacheHold(JoinPoint jp, final JointPointParameter parameter) {
        Cacheable ann = parameter.getMethod().getAnnotation(Cacheable.class);
        String[] cacheNames = ann.value();

        String key = null;
        CacheManager manager = CacheManagerFactory.getManager();
        if (!JwUtils.isEmpty(cacheNames)) {
            key = CacheUtils.getCacheKey(parameter, cacheNames, ann.prefix(), ann.join());
            if (StringUtils.isEmpty(key)) {
                return null;
            }
            for (String cacheName : cacheNames) {
                Cache cache = manager.getCache(cacheName);
                ValueWrapper wrapper = cache.get(key);
                if (wrapper == null) {
                    continue;
                }
                if (wrapper == JwValueWrapper.VOID) {
                    return null;
                }
                if (JwUtils.isValid(wrapper)) {
                    return wrapper.getValue();
                }
            }
        } else {
            LOGGER.warn("The cache name is not set in " + parameter);
        }

        Object result = null;
        try {
            result = parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
            if (!JwUtils.isEmpty(cacheNames)) {
                for (String cacheName : cacheNames) {
                    Cache cache = manager.getCache(cacheName);
                    cache.put(key, result);
                }
            }
        } catch (Throwable e) {
            LOGGER.error("Error raised when invorked method " + parameter, e);
        }
        return result;
    }

}
