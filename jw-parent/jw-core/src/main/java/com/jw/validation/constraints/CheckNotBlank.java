package com.jw.validation.constraints;

import java.lang.reflect.Array;
import java.util.Collection;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.NotBlank;

public class CheckNotBlank implements ConstraintValidator<NotBlank, Object> {

    @SuppressWarnings("unused")
    private NotBlank ann;

    @Override
    public void initialize(NotBlank constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isValid(Object value) {
        if(value == null) {
            return false;
        }
        if(value instanceof String) {
            return !((String) value).trim().isEmpty();
        }
        if(value instanceof Collection) {
            return !((Collection) value).isEmpty();
        }
        if(value instanceof Array) {
            return Array.getLength(value) > 0;
        }
        return true;
    }

}
