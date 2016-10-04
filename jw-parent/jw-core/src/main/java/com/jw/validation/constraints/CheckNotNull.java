package com.jw.validation.constraints;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.NotNull;

public class CheckNotNull implements ConstraintValidator<NotNull, Object> {

    @SuppressWarnings("unused")
    private NotNull ann;

    @Override
    public void initialize(NotNull constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value) {
        return value != null;
    }

}
