/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.hun.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 2090714647038636896L;
    /**
     * ID
     */
    private String userId;

    /**
     * 密码
     */
    private String password;

    private Integer type;


}
