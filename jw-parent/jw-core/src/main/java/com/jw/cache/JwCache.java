package com.jw.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jw.util.JwUtils;

public class JwCache implements Cache {
    private String name;

    /**
     * 有效时长, 默认30天
     */
    private long validity = 30 * 24 * 60 * 60 * 1000;

    private int capacity = 0;

    private Map<Object, ValueWrapper> map = new ConcurrentHashMap<Object, ValueWrapper>();

    public String getName() {
        return name;
    }

    public Object getNativeCache() {
        return this;
    }

    public ValueWrapper get(Object key) {
        return map.get(key);
    }

    public void put(Object key, Object value) {
        if (capacity > 0 && map.size() >= capacity) {
            // clear out date cache
            synchronized (map) {
                if (capacity > 0 && map.size() >= capacity) {
                    Set<Entry<Object, ValueWrapper>> entries = map.entrySet();
                    Iterator<Entry<Object, ValueWrapper>> iter = entries.iterator();
                    Entry<Object, ValueWrapper> entry;
                    while (iter.hasNext()) {
                        entry = iter.next();
                        if (!JwUtils.isValid(entry.getValue())) {
                            iter.remove();
                        }
                    }
                }
            }
        }
        if (capacity <= 0 || map.size() < capacity) {
            if (value == null) {
                map.put(key, JwValueWrapper.VOID);
            } else {
                long validTime = validity <= 0 ? 0 : System.currentTimeMillis() + validity * 1000;
                ValueWrapper w = new JwValueWrapper(value, validTime);
                map.put(key, w);
            }
        }
    }

    public void evict(Object key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    @Override
    public long getValidity() {
        return validity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValidity(long validity) {
        this.validity = validity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
