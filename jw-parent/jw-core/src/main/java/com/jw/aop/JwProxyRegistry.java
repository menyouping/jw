package com.jw.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.jw.aop.annotation.Aspect;
import com.jw.util.CollectionUtils;
import com.jw.util.ConfigUtils;
import com.jw.util.JwUtils;
import com.jw.util.PkgUtils;

public class JwProxyRegistry {
    private static final List<JoinPoint> JOIN_POINTS = CollectionUtils.newLinkedList();

    private JwProxyRegistry() {
    }

    static {
        Set<Class<?>> clazes = null;

        try {
            clazes = PkgUtils.findClazesByAnnotation(ConfigUtils.getString("aop.aspects.package.scan"), Aspect.class);
            register(clazes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {

    }

    private static synchronized void register(Set<Class<?>> clazes) {
        JOIN_POINTS.clear();
        for (Class<?> claze : clazes) {
            JOIN_POINTS.add(new JoinPoint(claze));
        }
    }

    public static List<JoinPoint> findMatchedAspects(Method targetMethod) {
        List<JoinPoint> list = CollectionUtils.newLinkedList();
        for (JoinPoint jp : JOIN_POINTS) {
            if (jp.match(targetMethod)) {
                list.add(jp);
            }
        }
        return list;
    }

}
