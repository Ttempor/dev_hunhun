package com.hun.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialAnalysisVO {
    //这个月学生就读人数
    private Integer studentCount;
    //这个月出勤人数
    private Integer attendCount;
    //这个月缺勤人数
    private Integer missCount;
    //这个月点名课消金额
    private BigDecimal callConsume;
    //这个月总课消金额
    private BigDecimal consume;
    //这个月学员总课消课时
    private Integer period;
}
