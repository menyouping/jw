package com.jw.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Component;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class JwProxy implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwProxy.class);

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        List<JoinPoint> jps = null;
        if (JwUtils.isAnnotated(method.getDeclaringClass(), Component.class)) {
            jps = JwProxyRegistry.findMatchedAspects(method);
        }
        if (JwUtils.isEmpty(jps)) {
            result = proxy.invokeSuper(obj, args);
        } else {
            JointPointParameter parameter = new JointPointParameter(obj, method, args, proxy);
            JoinPoint jpAround = null;
            int aroundCount = 0;
            for (JoinPoint jp : jps) {
                if (jp.hasBeforeMethod()) {
                    jp.proceedBefore(parameter);
                }
                if (jp.hasAroundMethod()) {
                    jpAround = jp;
                    aroundCount++;
                }
            }
            if (aroundCount > 1) {
                String msg = "There are more than one Aspect with around advice for method" + method.getName()
                        + " of class " + method.getDeclaringClass().getName();
                LOGGER.error(msg);
                throw new Exception(msg);

            }
            if (jpAround == null) {
                result = proxy.invokeSuper(obj, args);
            } else {
                result = jpAround.proceedAround(parameter);
            }

            for (JoinPoint jp : jps) {
                if (jp.hasAfterMethod()) {
                    jp.proceedAfter(parameter);
                }
            }
        }

        return result;
    }
}
