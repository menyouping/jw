package com.jw.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
    /**
     * Qualifier value for the specified cached operation.
     * <p>
     * May be used to determine the target cache (or caches), matching the
     * qualifier value (or the bean name(s)) of (a) specific bean definition.
     */
    String[] value();

    String prefix() default "";

    String join() default "";

}
