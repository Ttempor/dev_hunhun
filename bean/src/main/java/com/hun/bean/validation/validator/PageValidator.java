package com.hun.bean.validation.validator;

import com.hun.bean.validation.annotation.Page;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PageValidator implements ConstraintValidator<Page, Long> {
    /**
     * 页码校验
     */
    @Override
    public boolean isValid(Long page, ConstraintValidatorContext constraintValidatorContext) {
        return page != null && page <= 1000 && page > 0;
    }
}
