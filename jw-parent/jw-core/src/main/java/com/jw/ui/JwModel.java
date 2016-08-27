package com.jw.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class JwModel extends HashMap<String, Object> implements Model {
    
    public JwModel() {
    }

    public JwModel(String attributeName, Object attributeValue) {
        addAttribute(attributeName, attributeValue);
    }

    public JwModel(Object attributeValue) {
        addAttribute(attributeValue);
    }


    public JwModel addAttribute(String attributeName, Object attributeValue) {
        put(attributeName, attributeValue);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public JwModel addAttribute(Object attributeValue) {
        if (attributeValue instanceof Collection && ((Collection) attributeValue).isEmpty()) {
            return this;
        }
        return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
    }

    public JwModel addAllAttributes(Collection<?> attributeValues) {
        if (attributeValues != null) {
            for (Object attributeValue : attributeValues) {
                addAttribute(attributeValue);
            }
        }
        return this;
    }

    public JwModel addAllAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            putAll(attributes);
        }
        return this;
    }

    public JwModel mergeAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (String key : attributes.keySet()) {
                if (!containsKey(key)) {
                    put(key, attributes.get(key));
                }
            }
        }
        return this;
    }

    public boolean containsAttribute(String attributeName) {
        return containsKey(attributeName);
    }

    @Override
    public Map<String, Object> asMap() {
        return this;
    }

}
