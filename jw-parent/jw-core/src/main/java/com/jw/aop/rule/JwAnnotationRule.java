package com.jw.aop.rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.util.JwUtils;

public class JwAnnotationRule extends JwRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwAnnotationRule.class);

    public static final Pattern RULE_PATTERN = Pattern.compile("@annotation\\(([^)]+)\\)");

    private Class<Annotation> annotation = null;

    @SuppressWarnings("unchecked")
    private JwAnnotationRule(String rule) {
        super(rule);
        Matcher m = matcher(rule);
        if(!m.find()) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule);
            return;
        }
        try {
            String clazeName = m.group(1);
            annotation = (Class<Annotation>) Class.forName(clazeName);
        } catch (ClassNotFoundException e) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule, e);
        }
    }

    public static JwAnnotationRule parse(String rule) {
        JwAnnotationRule result = new JwAnnotationRule(rule);
        return result.isValid ? result : null;
    }

    public boolean match(Method targetMethod) {
        return JwUtils.isAnnotated(targetMethod, annotation);
    }

    public Class<Annotation> getAnnotation() {
        return annotation;
    }

    public static Matcher matcher(CharSequence rule) {
        return RULE_PATTERN.matcher(rule);
    }
}
