package com.hun.bean.validation.annotation;

import com.hun.bean.validation.validator.PageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {PageValidator.class}
)
public @interface Page {
    String message() default "页码参数错误,最小为1,最大页为1000";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
