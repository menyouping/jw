package com.jay.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JoinPoint;
import com.jw.aop.JointPointParameter;
import com.jw.aop.annotation.After;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Aspect;
import com.jw.aop.annotation.Before;
import com.jw.aop.annotation.Pointcut;

@Aspect
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.jay.aop.annotation.Log)")
    public void logPointcut() {

    }

    @Before("logPointcut()")
    public void beforeLogHold(JoinPoint jp, JointPointParameter parameter) {
        LOGGER.info("Before method invorked!");
    }

    @Around("logPointcut()")
    public Object aroundLogHold(JoinPoint jp, JointPointParameter parameter) {
        LOGGER.info("Around method start!");
        Object result = null;
        try {
            result = parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
        } catch (Throwable e) {
            LOGGER.error("Error raised when invorked method " + parameter, e);
        }

        LOGGER.info("Around method end!");
        return result;
    }

    @After("logPointcut()")
    public void afterLogHold(JoinPoint jp, JointPointParameter parameter) {
        LOGGER.info("After method invorked!");
    }
}
