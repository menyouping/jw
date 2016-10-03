package com.jw.validation.constraints;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.LessThan;

public class CheckLessThan implements ConstraintValidator<LessThan, Integer> {

    private LessThan ann;

    @Override
    public void initialize(LessThan constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value) {
        if (value != null) {
            if (value instanceof Number) {
                if (ann.inclusive()) {
                    return ((Number) value).doubleValue() - ann.value() <= 0d;
                } else {
                    return ((Number) value).doubleValue() - ann.value() < 0d;
                }
            }
        }
        return false;
    }

}
