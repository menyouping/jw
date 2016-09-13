package com.jw.aop;

import javax.servlet.http.HttpServletRequest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class JwProxyFactory {

    private static final JwProxy proxy = new JwProxy();

    @SuppressWarnings("unchecked")
    public static <T> T getProxyInstance(Class<T> claze) {
        if (HttpServletRequest.class.isAssignableFrom(claze)) {
            return (T) JwProxyFactory.getCglibProxy(JwHttpServletRequest.class, proxy);
        }
        return JwProxyFactory.getCglibProxy(claze, proxy);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCglibProxy(Class<T> claze, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claze);
        enhancer.setCallback(interceptor);
        return (T) enhancer.create();
    }

}
