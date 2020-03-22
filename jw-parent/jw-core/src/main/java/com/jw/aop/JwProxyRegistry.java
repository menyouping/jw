package com.jw.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.annotation.Aspect;
import com.jw.util.CollectionUtils;
import com.jw.util.ConfigUtils;
import com.jw.util.PkgUtils;

public class JwProxyRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(JwProxyRegistry.class);

    private static final String AOP_PACKAGE = "aop.aspects.package.scan";
    private static final List<JoinPoint> JOIN_POINT_LIST = CollectionUtils.newLinkedList();

    private JwProxyRegistry() {
    }

    public static void init() {
        Set<Class<?>> clazes = null;

        try {
            String pkg = ConfigUtils.getString(AOP_PACKAGE);
            clazes = PkgUtils.findClazesByAnnotation(pkg, Aspect.class);
            clazes.forEach(claze -> JOIN_POINT_LIST.add(new JoinPoint(claze)));
        } catch (Exception e) {
            LOG.error("加载AOP失败", e);
        }
    }

    public static List<JoinPoint> match(Method targetMethod) {
        return JOIN_POINT_LIST.stream().filter(jp -> jp.match(targetMethod)).collect(Collectors.toList());
    }

}
