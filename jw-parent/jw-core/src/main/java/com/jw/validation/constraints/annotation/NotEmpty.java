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
import com.jw.validation.constraints.CheckNotEmpty;

/**
 * Asserts that the annotated string, collection, map or array is not
 * {@code null} or empty.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@Documented
@Constraint(validatedBy = {CheckNotEmpty.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface NotEmpty {
    String message() default "{org.hibernate.validator.constraints.NotEmpty.message}";

    Class<?>[] groups() default {};

    /**
     * Defines several {@code @NotEmpty} annotations on the same element.
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        NotEmpty[] value();
    }
}
