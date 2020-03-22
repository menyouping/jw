package com.jw.aop.rule;

import java.lang.reflect.Method;

public abstract class JwRule {

    protected String rule;

    public JwRule(String rule) {
        this.rule = rule;
    }

    public abstract boolean match(Method targetMethod);
}
