package com.jw.aop.rule;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 复合规则
 * @author jay
 *
 */
public class JwCompoundExecutionRule extends JwRule {
    public static final Pattern RULE_PATTERN = Pattern.compile("execution\\((.*)\\)(\\s+or\\s+execution\\((.*)\\))+");

    private List<JwExecutionRule> rules = new LinkedList<JwExecutionRule>();

    private JwCompoundExecutionRule(String rule) {
        super(rule);
        init();
    }

    public void init() {
        Matcher m = matcher(rule);
        if (!m.find()) {
            throw new IllegalArgumentException(String.format("切点规则%s编译失败", rule));
        }
        String[] list = rule.split("\\s+or\\s+");
        JwExecutionRule executionRule;
        for (String r : list) {
            executionRule = JwExecutionRule.parse(r.trim());
            rules.add(executionRule);
        }
    }

    public static JwCompoundExecutionRule parse(String rule) {
        return new JwCompoundExecutionRule(rule);
    }

    public boolean match(Method targetMethod) {
        for (JwExecutionRule rule : rules) {
            if (rule.match(targetMethod)) {
                return true;
            }
        }
        return false;
    }

    public List<JwExecutionRule> getRules() {
        return rules;
    }

    public static Matcher matcher(CharSequence rule) {
        return RULE_PATTERN.matcher(rule);
    }
}
