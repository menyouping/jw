package com.jw.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.annotation.After;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Before;
import com.jw.aop.annotation.Pointcut;
import com.jw.aop.rule.JwRule;
import com.jw.aop.rule.JwRuleFactory;
import com.jw.util.ArgumentChecker;
import com.jw.util.ReflectionUtils;
import com.jw.util.StringUtils;

public class JoinPoint {
    private static final Logger LOG = LoggerFactory.getLogger(JoinPoint.class);

    private Class<?> aspectClaze = null;

    private Object aspectInstance = null;

    private Method pointcutMethod = null;

    private Method beforeMethod = null;

    private Method aroundMethod = null;

    private Method afterMethod = null;

    private JwRule rule = null;

    public JoinPoint(Class<?> aspectClaze) {
        List<Method> pointcutMethods = ReflectionUtils.findMethods(aspectClaze, Pointcut.class);
        ArgumentChecker.notEmpty(pointcutMethods);
        ArgumentChecker.notTrue(pointcutMethods.size() > 1);

        this.aspectClaze = aspectClaze;
        try {
            this.aspectInstance = aspectClaze.newInstance();
        } catch (Exception e) {
            LOG.error(String.format("类%s创建对象失败", aspectClaze.getName()), e);
        }
        this.pointcutMethod = pointcutMethods.get(0);

        Pointcut ann = pointcutMethod.getAnnotation(Pointcut.class);
        if (!StringUtils.isEmpty(ann.value())) {
            rule = JwRuleFactory.parse(ann.value());
            if (rule == null) {
                throw new IllegalArgumentException("切点配置不正确!");
            }
        } else {
            throw new IllegalArgumentException("切点配置不能为空!");
        }
        String pointcutMethodName = pointcutMethod.getName() + "()";
        Method[] mds = aspectClaze.getDeclaredMethods();
        for (Method md : mds) {
            Before before = md.getAnnotation(Before.class);
            if (md == pointcutMethod || (before != null && pointcutMethodName.equals(before.value()))) {
                this.beforeMethod = md;
                continue;
            }
            Around around = md.getAnnotation(Around.class);
            if (md == pointcutMethod || (around != null && pointcutMethodName.equals(around.value()))) {
                this.aroundMethod = md;
                continue;
            }
            After after = md.getAnnotation(After.class);
            if (md == pointcutMethod || (after != null && pointcutMethodName.equals(after.value()))) {
                this.afterMethod = md;
                continue;
            }
        }
    }

    public boolean match(Method targetMethod) {
        return rule.match(targetMethod);
    }

    public void proceedBefore(JointPointParameter parameter) {
        if (!hasBeforeMethod())
            return;

        try {
            Object[] adviceArgs = new Object[] { this, parameter };
            beforeMethod.invoke(aspectInstance, adviceArgs);
        } catch (Throwable e) {
            LOG.error("Error raised when call beforeMethod", e);
        }
    }

    public Object proceedAround(JointPointParameter parameter) {
        if (!hasAroundMethod())
            return null;

        Object result = null;
        try {
            Object[] adviceArgs = new Object[] { this, parameter };
            result = aroundMethod.invoke(aspectInstance, adviceArgs);
        } catch (Throwable e) {
            LOG.error("Error raised when call targgetMethod", e);
        }
        return result;
    }

    public void proceedAfter(JointPointParameter parameter) {
        if (!hasAfterMethod())
            return;

        try {
            Object[] adviceArgs = new Object[] { this, parameter };
            afterMethod.invoke(aspectInstance, adviceArgs);
        } catch (Throwable e) {
            LOG.error("Error raised when call targgetMethod", e);
        }
    }

    public Class<?> getAspectClaze() {
        return aspectClaze;
    }

    public void setAspectClaze(Class<?> aspectClaze) {
        this.aspectClaze = aspectClaze;
    }

    public Object getAspectInstance() {
        return aspectInstance;
    }

    public void setAspectInstance(Object aspectInstance) {
        this.aspectInstance = aspectInstance;
    }

    public Method getPointcutMethod() {
        return pointcutMethod;
    }

    public void setPointcutMethod(Method pointcutMethod) {
        this.pointcutMethod = pointcutMethod;
    }

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public Method getAroundMethod() {
        return aroundMethod;
    }

    public void setAroundMethod(Method aroundMethod) {
        this.aroundMethod = aroundMethod;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public JwRule getRule() {
        return rule;
    }

    public boolean hasBeforeMethod() {
        return beforeMethod != null;
    }

    public boolean hasAroundMethod() {
        return aroundMethod != null;
    }

    public boolean hasAfterMethod() {
        return afterMethod != null;
    }

}
