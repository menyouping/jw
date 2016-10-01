package com.jw.cache;

public interface Cache {
    /**
     * Return the cache name.
     */
    String getName();

    /**
     * 有效时长, 单位:秒
     */
    long getValidity();

    /**
     * Return the the underlying native cache provider.
     */
    Object getNativeCache();

    /**
     * Return the value to which this cache maps the specified key. Returns
     * {@code null} if the cache contains no mapping for this key.
     * 
     * @param key
     *            key whose associated value is to be returned.
     * @return the value to which this cache maps the specified key, or
     *         {@code null} if the cache contains no mapping for this key
     */
    ValueWrapper get(Object key);

    /**
     * Associate the specified value with the specified key in this cache.
     * <p>
     * If the cache previously contained a mapping for this key, the old value
     * is replaced by the specified value.
     * 
     * @param key
     *            the key with which the specified value is to be associated
     * @param value
     *            the value to be associated with the specified key
     */
    void put(Object key, Object value);

    /**
     * Evict the mapping for this key from this cache if it is present.
     * 
     * @param key
     *            the key whose mapping is to be removed from the cache
     */
    void evict(Object key);

    /**
     * Remove all mappings from the cache.
     */
    void clear();

}
