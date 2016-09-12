package com.jw.aop;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;

import net.sf.cglib.proxy.MethodProxy;

public class JointPointParameter {
    private Object obj;

    private Method method;

    private Object[] args;

    private MethodProxy proxy;

    public JointPointParameter(Object obj, Method method, Object[] args, MethodProxy proxy) {
        super();
        this.obj = obj;
        this.method = method;
        this.args = args;
        this.proxy = proxy;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MethodProxy getProxy() {
        return proxy;
    }

    public void setProxy(MethodProxy proxy) {
        this.proxy = proxy;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
