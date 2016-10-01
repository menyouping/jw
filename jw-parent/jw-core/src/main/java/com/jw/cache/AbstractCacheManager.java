package com.jw.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractCacheManager implements CacheManager {
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);

    public void afterPropertiesSet() {
        Collection<? extends Cache> caches = loadCaches();

        // preserve the initial order of the cache names
        this.cacheMap.clear();
        for (Cache cache : caches) {
            this.cacheMap.put(cache.getName(), decorateCache(cache));
        }
    }

    protected final void addCache(Cache cache) {
        this.cacheMap.put(cache.getName(), decorateCache(cache));
    }

    /**
     * Decorate the given Cache object if necessary.
     * 
     * @param cache
     *            the Cache object to be added to this CacheManager
     * @return the decorated Cache object to be used instead, or simply the
     *         passed-in Cache object by default
     */
    protected Cache decorateCache(Cache cache) {
        return cache;
    }

    public Cache getCache(String name) {
        return this.cacheMap.get(name);
    }

    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    /**
     * Load the caches for this cache manager. Occurs at startup. The returned
     * collection must not be null.
     */
    protected abstract Collection<? extends Cache> loadCaches();
}
