package com.hun.bean.app;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class APPUserInfoVO {
    private Long studentId;
    private String name;
    private String sex;
    private String phone;
    private BigDecimal balance;
    private BigDecimal lockBalance;
}
