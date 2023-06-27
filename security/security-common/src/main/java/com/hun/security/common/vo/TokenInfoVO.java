/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.hun.security.common.vo;

import com.hun.security.common.enums.SysTypeEnum;
import lombok.Data;

/**
 * token信息，该信息用户返回给前端，前端请求携带accessToken进行用户校验
 */
@Data
public class TokenInfoVO {

    //登录吼的 token
    private String accessToken;

    //刷新后的 token
    private String refreshToken;

    //账号类型
    private SysTypeEnum type;

    //账号id
    private String id;

    //过期时间
    private Integer expiresIn;
}
