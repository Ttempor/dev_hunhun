package com.hun.bean.validation.annotation;

import com.hun.bean.validation.validator.SexValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {SexValidator.class}
)
public @interface Sex {
    String message() default "性别只能是男或女";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
