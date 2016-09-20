package com.jw.aop.rule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwCompoundExecutionRule extends JwRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwCompoundExecutionRule.class);

    public static final Pattern RULE_PATTERN = Pattern.compile("execution\\((.*)\\)(\\s+or\\s+execution\\((.*)\\))+");

    private List<JwExecutionRule> rules = null;

    private JwCompoundExecutionRule(String rule) {
        super(rule);
        init();
    }

    public void init() {
        Matcher m = matcher(rule);
        if (!m.find()) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule);
            return;
        }
        String[] list = rule.split("\\s+or\\s+");
        rules = new ArrayList<JwExecutionRule>();
        JwExecutionRule executionRule;
        for (String r : list) {
            executionRule = JwExecutionRule.parse(r.trim());
            if (executionRule == null) {
                isValid = false;
                break;
            }
            rules.add(executionRule);
        }
    }

    public static JwCompoundExecutionRule parse(String rule) {
        JwCompoundExecutionRule result = new JwCompoundExecutionRule(rule);
        return result.isValid ? result : null;
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
