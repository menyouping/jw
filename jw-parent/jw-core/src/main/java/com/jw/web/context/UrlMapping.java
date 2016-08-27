package com.jw.web.context;

import java.lang.reflect.Method;

public class UrlMapping {

    public UrlMapping(Method method) {
        super();
        this.method = method;
    }

    private Method method;

    public Class<?> getClaze() {
        return method.getDeclaringClass();
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "UrlMapping [claze=" + getClaze().getSimpleName() + ", method=" + method.getName() + "]";
    }
}
