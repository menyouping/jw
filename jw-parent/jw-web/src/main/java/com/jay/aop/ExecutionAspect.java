package com.jay.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.aop.JoinPoint;
import com.jw.aop.JointPointParameter;
import com.jw.aop.annotation.Around;
import com.jw.aop.annotation.Aspect;
import com.jw.aop.annotation.Pointcut;

@Aspect
public class ExecutionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionAspect.class);

    @Pointcut("execution(* com.jay..*Controller.create()) or execution(* com.jay..*Controller.save())")
    public void execPointcut() {

    }

    @Around("execPointcut()")
    public Object aroundExecHold(JoinPoint jp, JointPointParameter parameter) {
        LOGGER.info("Before save");
        Object result = null;
        try {
            result = parameter.getProxy().invokeSuper(parameter.getObj(), parameter.getArgs());
        } catch (Throwable e) {
            LOGGER.error("Error raised when invorked method " + parameter, e);
        }

        LOGGER.info("After save!");
        return result;
    }
}
