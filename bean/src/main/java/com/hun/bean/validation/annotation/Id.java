package com.hun.bean.validation.annotation;

import com.hun.bean.validation.validator.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IdValidator.class}
)
public @interface Id {
    String message() default "id错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
