package com.jw.aop.rule;

import java.lang.reflect.Method;

public abstract class JwRule {

    protected String rule;

    /**
     * 表示rule规则用户是否正确设置
     */
    protected boolean isValid = true;

    public JwRule(String rule) {
        this.rule = rule;
    }

    public abstract boolean match(Method targetMethod);
}
