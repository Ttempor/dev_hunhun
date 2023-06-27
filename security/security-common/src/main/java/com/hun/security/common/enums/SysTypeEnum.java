/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.hun.security.common.enums;

import com.hun.bean.entity.base.Employee;
import com.hun.bean.model.User;

/**
 * 系统类型
 */
public enum SysTypeEnum {

    /**
     * 普通用户系统
     */
    ORDINARY(0),

    /**
     * 员工系统
     */
    Employee(1),

    /**
     * 后台
     */
    ADMIN(2),
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    SysTypeEnum(Integer value) {
        this.value = value;
    }

    public static boolean isEmployee(User user) {
        return user.getType().equals(Employee.value);
    }
    public static boolean isORDINARY(User user) {
        return user.getType().equals(ORDINARY.value);
    }
    public static boolean isADMIN(User user) {
        return user.getType().equals(ADMIN.value);
    }

    public static SysTypeEnum parse(User user) {
        if (isORDINARY(user)) {
            return ORDINARY;
        }
        if (isEmployee(user)) {
            return Employee;
        }
        if (isADMIN(user)) {
            return ADMIN;
        }
        return null;
    }

}
