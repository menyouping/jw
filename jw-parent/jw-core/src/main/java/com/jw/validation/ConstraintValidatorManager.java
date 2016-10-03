package com.jw.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.jw.util.JwUtils;

public class ConstraintValidatorManager {

    @SuppressWarnings("rawtypes")
    public static <T> ValidErrors validate(Object t) throws Exception {
        Field[] fields = t.getClass().getDeclaredFields();
        if (JwUtils.isEmpty(fields)) {
            return null;
        }
        boolean ok = true;

        ValidErrors error = new ValidErrors();
        Annotation[] anns = null;
        Constraint constraint = null;
        ConstraintValidator<?, ?> validator = null;
        Class<? extends ConstraintValidator>[] validatorClazes = null;
        Object fieldValue = null;
        for (Field field : fields) {
            anns = field.getAnnotations();
            if(JwUtils.isEmpty(anns)) {
                continue;
            }
            field.setAccessible(true);
            fieldValue = field.get(t);
            for (Annotation ann : anns) {
                constraint = ann.annotationType().getAnnotation(Constraint.class);
                if (constraint == null) {
                    continue;
                }
                validatorClazes = constraint.validatedBy();
                if (JwUtils.isEmpty(validatorClazes)) {
                    continue;
                }
                for (Class<? extends ConstraintValidator> validatorClaze : validatorClazes) {
                    validator = validatorClaze.newInstance();
                    if (!isValid(validator, ann, fieldValue)) {
                        ok = false;
                        error.put(field.getName(), (String) ann.annotationType().getMethod("message").invoke(ann));
                    }
                }
            }
        }
        return ok ? null : error;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static <A extends Annotation, T> boolean isValid(ConstraintValidator validator, A ann, T fieldValue) {
        validator.initialize(ann);
        return validator.isValid(fieldValue);
    }
}
