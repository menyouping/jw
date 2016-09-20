package com.jw.aop.rule;

import java.lang.reflect.Modifier;

public enum MethodModifier {

    ALL("*"), //
    PUBLIC("public"), //
    PROTECTED("protected"), //
    PRIVATE("private");

    private String value;

    MethodModifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMatch(int modifier) {
        switch (this) {
        case ALL:
            return true;
        case PUBLIC:
            return Modifier.isPublic(modifier);
        case PROTECTED:
            return Modifier.isProtected(modifier);
        case PRIVATE:
            return Modifier.isPrivate(modifier);
        default:
            break;
        }
        return false;
    }
}
