package com.jw.validation.constraints;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.GreaterThan;

public class CheckGreaterThan implements ConstraintValidator<GreaterThan, Object> {

    private GreaterThan ann;

    @Override
    public void initialize(GreaterThan constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value) {
        if (value != null) {
            if (value instanceof Number) {
                if (ann.inclusive()) {
                    return ((Number) value).doubleValue() - ann.value() >= 0d;
                } else {
                    return ((Number) value).doubleValue() - ann.value() > 0d;
                }
            }
        }
        return false;
    }

}
