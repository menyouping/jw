package com.jw.ui;

import java.util.Collection;
import java.util.Map;

public interface Model {
    Model addAttribute(String attributeName, Object attributeValue);

    Model addAttribute(Object attributeValue);

    Model addAllAttributes(Collection<?> attributeValues);

    Model addAllAttributes(Map<String, ?> attributes);

    Model mergeAttributes(Map<String, ?> attributes);

    boolean containsAttribute(String attributeName);

    Map<String, Object> asMap();
}
