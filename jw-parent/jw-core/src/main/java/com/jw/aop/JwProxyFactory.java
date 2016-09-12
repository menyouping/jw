package com.jw.aop;

public class JwProxyFactory {

    private static final JwProxy proxy = new JwProxy();

    public static <T> T getProxyInstance(Class<T> claze) {
        return proxy.getBean(claze);
    }
}
