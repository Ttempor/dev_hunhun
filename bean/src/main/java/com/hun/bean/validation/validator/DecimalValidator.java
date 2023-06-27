package com.hun.bean.validation.validator;

import com.hun.bean.validation.annotation.Decimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class DecimalValidator implements ConstraintValidator<Decimal, BigDecimal> {
    /**
     * BigDecimal的校验
     * 要求有效位最大10
     * 小数位8
     * 校验小数点是否是两位数进度
     */
    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        if (bigDecimal != null && bigDecimal.scale() == 2 && bigDecimal.precision() <= 10) {
            return bigDecimal.compareTo(new BigDecimal("0.00")) != -1;
        }
        return false;

    }
}
