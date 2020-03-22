package com.jw.validation;

import java.util.Collection;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.jw.util.CollectionUtils;

public class ValidErrors extends TreeMap<String, Collection<String>> {

    private static final long serialVersionUID = -4413966110474101576L;

    public void put(String key, String value) {
        Collection<String> c = get(key);
        if (c == null) {
            c = CollectionUtils.newLinkedList();
            put(key, c);
        }
        c.add(value);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
