package com.jw.ui;

import java.util.Collection;

import com.jw.util.StringUtils;

public class Conventions {

    @SuppressWarnings("rawtypes")
    public static String getVariableName(Object attributeValue) {
        if (attributeValue instanceof Collection) {
            return StringUtils.lowerFirst(((Collection) attributeValue).iterator().next().getClass().getSimpleName());
        }
        return StringUtils.lowerFirst(attributeValue.getClass().getSimpleName());
    }

}
