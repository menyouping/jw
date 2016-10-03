package com.jw.validation.constraints.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jw.validation.Constraint;
import com.jw.validation.constraints.CheckGreaterThan;

@Documented
@Constraint(validatedBy = { CheckGreaterThan.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface GreaterThan {
    /**
     * 是否包含等于
     * 
     * @return
     */
    boolean inclusive() default true;

    int value() default 0;

    String message() default "{validation.format.greaterThan}";

    Class<?>[] groups() default {};

}
