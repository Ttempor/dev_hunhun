package com.hun.bean.validation.validator;

import com.hun.bean.validation.annotation.Sex;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 已验空
 * 校验性别，只能是男或女
 */
public class SexValidator implements ConstraintValidator<Sex, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasLength(s) && (s.equals("男") || s.equals("女"));
    }
}