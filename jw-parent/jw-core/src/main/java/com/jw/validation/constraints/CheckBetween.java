package com.jw.validation.constraints;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.Between;

public class CheckBetween implements ConstraintValidator<Between, Number> {

    private Between ann;

    @Override
    public void initialize(Between constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @Override
    public boolean isValid(Number value) {
        if (value != null) {
            if (value instanceof Number) {
                if (ann.inclusive()) {
                    return ((Number) value).doubleValue() - ann.min() >= 0d
                            && ((Number) value).doubleValue() - ann.max() <= 0d;
                } else {
                    return ((Number) value).doubleValue() - ann.min() > 0d
                            && ((Number) value).doubleValue() - ann.max() < 0d;
                }
            }
        }
        return false;
    }

}
