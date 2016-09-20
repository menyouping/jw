package com.jw.aop.rule;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwExecutionRule extends JwRule {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwExecutionRule.class);

    public static final Pattern RULE_PATTERN = Pattern.compile("execution\\((.*)\\)");

    private Pattern EXP_PATTERN = Pattern.compile("(.*)\\((.*)\\)");

    /**
     * *,public,protected,private
     */
    private MethodModifier modifier = MethodModifier.ALL;
    /**
     * *或具体的类型名
     */
    private String returnType;
    /**
     * 类名全路径的正则表达式
     */
    private Pattern clazeNamePattern;
    /**
     * 方法名正则表达式
     */
    private Pattern methodNamePattern;
    private String[] paramClazes;

    private JwExecutionRule(String rule) {
        super(rule);
        init();
    }

    private void init() {
        Matcher m = matcher(rule);
        if (!m.find()) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule);
            return;
        }

        String r = m.group(1);
        String[] parts = r.split("\\s+", 3);
        if (parts.length < 2) {
            isValid = false;
            LOGGER.error("Rule is invalid: " + rule);
            return;
        }
        int i = 0;
        if (parts.length == 3) {
            try {
                modifier = MethodModifier.valueOf(parts[0]);
            } catch (Exception e) {
                isValid = false;
                LOGGER.error("Error raised when parse rule" + rule, e);
                return;
            }
            i++;
        }
        returnType = parts[i];
        String exp = parts[i + 1];
        m = EXP_PATTERN.matcher(exp);
        if (!m.find()) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule);
            return;
        }
        String strParamClazes = m.group(2).trim();
        paramClazes = strParamClazes.isEmpty() ? new String[0] : strParamClazes.split("\\s*,\\s*");
        String clazeName = m.group(1);
        int index = clazeName.lastIndexOf(".");
        if (index < 1 || index == clazeName.length() - 1 || '.' == clazeName.charAt(index - 1)) {
            isValid = false;
            LOGGER.error("Error raised when parse rule" + rule);
            return;
        }
        clazeName = clazeName.replace("*", "([\\w]*)");
        clazeName = clazeName.replace("..", "([.\\w]*)");
        index = clazeName.lastIndexOf(".");
        clazeNamePattern = Pattern.compile(clazeName.substring(0, index));
        methodNamePattern = Pattern.compile(clazeName.substring(index + 1));
    }

    public static JwExecutionRule parse(String rule) {
        JwExecutionRule result = new JwExecutionRule(rule);
        return result.isValid ? result : null;
    }

    public boolean match(Method targetMethod) {
        if (!methodNamePattern.matcher(targetMethod.getName()).find()) {
            return false;
        }
        if (!clazeNamePattern.matcher(targetMethod.getDeclaringClass().getName()).find()) {
            return false;
        }
        if (!modifier.isMatch(targetMethod.getModifiers())) {
            return false;
        }
        if (!"*".equals(returnType) && !returnType.equals(targetMethod.getReturnType().getSimpleName())) {
            return false;
        }
        Class<?>[] parameterizedTypes = targetMethod.getParameterTypes();
        if (paramClazes.length == 0 && parameterizedTypes.length == 0) {
            return true;
        }
        if (paramClazes.length == 1 && "..".equals(paramClazes[0].trim()) && parameterizedTypes.length > 0) {
            return true;
        }
        if (paramClazes.length != parameterizedTypes.length) {
            return false;
        }
        for (int i = paramClazes.length - 1; i > -1; i--) {
            if ("..".equals(paramClazes[i]))
                continue;
            if (!paramClazes[i].equals(parameterizedTypes[i].getSimpleName())) {
                return false;
            }
        }
        return true;
    }

    public MethodModifier getModifier() {
        return modifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public Pattern getClazeNamePattern() {
        return clazeNamePattern;
    }

    public Pattern getMethodNamePattern() {
        return methodNamePattern;
    }

    public String[] getParamClazes() {
        return paramClazes;
    }

    public static Matcher matcher(CharSequence rule) {
        return RULE_PATTERN.matcher(rule);
    }
}
