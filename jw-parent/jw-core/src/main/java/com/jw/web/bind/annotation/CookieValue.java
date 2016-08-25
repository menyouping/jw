package com.jw.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CookieValue {

    /**
     * The name of the cookie to bind to.
     */
    String value() default "";

    /**
     * Whether the header is required.
     * <p>Default is {@code true}, leading to an exception being thrown
     * in case the header is missing in the request. Switch this to
     * {@code false} if you prefer a {@code null} in case of the
     * missing header.
     * <p>Alternatively, provide a {@link #defaultValue}, which implicitly sets
     * this flag to {@code false}.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback. Supplying a default value implicitly
     * sets {@link #required} to {@code false}.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;

}
