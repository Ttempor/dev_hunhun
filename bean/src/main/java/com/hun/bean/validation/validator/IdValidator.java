package com.hun.bean.validation.validator;

import com.hun.bean.validation.annotation.Id;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 已验空
 * 校验id,要求是整型且>0
 * 可以校验Long类型 和String类型
 */
public class IdValidator implements ConstraintValidator<Id, Object> {
    /**
     * 判空校验
     * 是字符串转换为long
     * 是long则不转换
     * 判断long是否>0
     * @param objectId e
     * @param constraintValidatorContext e
     * @return e
     */
    @Override
    public boolean isValid(Object objectId, ConstraintValidatorContext constraintValidatorContext) {
        if (objectId == null) {
            return false;
        }
        long id;
        if (objectId instanceof String s) {
            try {
                id = Long.parseLong(s);
            } catch (Exception e) {
                return false;
            }
        } else if (objectId instanceof Long){
            id = (Long) objectId;
        } else {
            return false;
        }
        return id >= 0;
    }
}