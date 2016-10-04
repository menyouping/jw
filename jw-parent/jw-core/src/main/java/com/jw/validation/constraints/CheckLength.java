package com.jw.validation.constraints;

import com.jw.validation.ConstraintValidator;
import com.jw.validation.constraints.annotation.Length;

public class CheckLength implements ConstraintValidator<Length, String> {

    private Length ann;

    @Override
    public void initialize(Length constraintAnnotation) {
        this.ann = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value) {
        if (value != null) {
            return value.length() >= ann.min() && value.length() <= ann.max();
        }
        return ann.min() <= 0;
    }

}
