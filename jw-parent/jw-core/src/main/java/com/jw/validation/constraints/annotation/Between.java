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
import com.jw.validation.constraints.CheckBetween;

@Documented
@Constraint(validatedBy = { CheckBetween.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Between {
    /**
     * 是否包含等于
     * 
     * @return
     */
    boolean inclusive() default true;

    int min();

    int max();

    String message() default "{validation.format.between}";

    Class<?>[] groups() default {};

}
