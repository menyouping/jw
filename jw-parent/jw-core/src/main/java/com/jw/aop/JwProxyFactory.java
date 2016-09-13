package com.jw.aop;

import net.sf.cglib.proxy.Enhancer;

public class JwProxyFactory {

    private static final JwProxy proxy = new JwProxy();

    @SuppressWarnings("unchecked")
    public static <T> T getProxyInstance(Class<T> claze) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claze);
        enhancer.setCallback(proxy);
        return (T) enhancer.create();
    }

}
