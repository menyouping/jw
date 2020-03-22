package com.jw.aop.rule;

import java.util.regex.Matcher;

public class JwRuleFactory {

    public static JwRule parse(String rule) {
        Matcher m = JwAnnotationRule.matcher(rule);
        if (m.find()) {
            return JwAnnotationRule.parse(rule);
        }
        m = JwCompoundExecutionRule.matcher(rule);
        if (m.find()) {
            return JwCompoundExecutionRule.parse(rule);
        }
        m = JwExecutionRule.matcher(rule);
        if (m.find()) {
            return JwExecutionRule.parse(rule);
        }
        throw new IllegalArgumentException(String.format("规则%s配置错误", rule));
    }
}
