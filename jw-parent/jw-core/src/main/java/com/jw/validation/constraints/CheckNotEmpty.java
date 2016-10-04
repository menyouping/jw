package com.jw.validation.constraints;

import java.lang.reflect.Array;
import java.util.Collection;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.NotEmpty;

public class CheckNotEmpty implements ConstraintValidator<NotEmpty, Object> {

    @SuppressWarnings("unused")
    private NotEmpty ann;

    @Override
    public void initialize(NotEmpty constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isValid(Object value) {
        if(value == null) {
            return false;
        }
        if(value instanceof String) {
            return !((String) value).isEmpty();
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
