package com.jw.aop.rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jw.util.AnnotationUtils;

public class JwAnnotationRule extends JwRule {
    /**
     * 匹配@annotation(xxx)
     */
    public static final Pattern RULE_PATTERN = Pattern.compile("@annotation\\(([^)]+)\\)");

    private Class<Annotation> ann = null;

    @SuppressWarnings("unchecked")
    private JwAnnotationRule(String rule) {
        super(rule);
        Matcher m = matcher(rule);
        if (!m.find()) {
            throw new IllegalArgumentException(String.format("切点规则%s编译失败", rule));
        }
        try {
            String clazeName = m.group(1);
            ann = (Class<Annotation>) Class.forName(clazeName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("切点规则%s编译失败", rule));
        }
    }

    public static JwAnnotationRule parse(String rule) {
        JwAnnotationRule result = new JwAnnotationRule(rule);
        return result;
    }

    public boolean match(Method targetMethod) {
        return AnnotationUtils.isAnnotated(targetMethod, ann);
    }

    public Class<Annotation> getAnnotation() {
        return ann;
    }

    public static Matcher matcher(CharSequence rule) {
        return RULE_PATTERN.matcher(rule);
    }
}
