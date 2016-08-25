package com.jw.web.context;

import java.lang.reflect.Method;

public class UrlMapping {

    public UrlMapping(Class<?> claze, Method method) {
        super();
        this.claze = claze;
        this.method = method;
    }

    private Class<?> claze;

    private Method method;

    public Class<?> getClaze() {
        return claze;
    }

    public void setClaze(Class<?> claze) {
        this.claze = claze;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    
    @Override
    public String toString() {
        return "UrlMapping [claze=" + claze.getSimpleName() + ", method=" + method.getName() + "]";
    }
}
