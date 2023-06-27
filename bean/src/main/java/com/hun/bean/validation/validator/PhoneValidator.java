package com.hun.bean.validation.validator;




import com.hun.bean.validation.annotation.Phone;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  已验空
 *  校验电话
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    /**
     * 验空,长度11,不为负数的整形
     *
     * @param s e
     * @param constraintValidatorContext e
     * @return e
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasLength(s) || s.trim().length() != 11) {
            return false;
        }
        s = s.trim();
        if (s.length() != 11) {
            return false;
        }
        try {
            if (Long.parseLong(s) <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}