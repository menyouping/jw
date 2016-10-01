package com.jw.cache;

public interface ValueWrapper {
    /**
     * 有效期至
     * @return
     */
    long getValidTime();
    
    Object getValue();
}
