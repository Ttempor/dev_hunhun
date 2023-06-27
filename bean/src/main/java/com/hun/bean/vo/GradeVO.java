package com.hun.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeVO {
    private Long gradeId;
    private String gradeName;
    private Integer gradeState;
    private Long chargeMethodId;
    private BigDecimal price;
    private String employeeName;
}
