/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.hun.security.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用于登陆传递账号密码
 */
@Data
public class AuthenticationDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "userName不能为空")
    protected String userName;

    /**
     * 密码
     */
    @NotBlank(message = "passWord不能为空")
    protected String passWord;

}
