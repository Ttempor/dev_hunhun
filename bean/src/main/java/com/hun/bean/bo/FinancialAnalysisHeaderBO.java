package com.hun.bean.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialAnalysisHeaderBO {
    //出勤人数
    private Integer attendCount;
    //缺勤人数
    private Integer missCount;
    //课消课时, 不包括缺勤, 包括套餐
    private Integer consumePeriod;
    //课消总收入包括套餐
    private BigDecimal courseCancelIncome;
    //点名课消总收入, 不包括套餐
    private BigDecimal callCourseCancelIncome;

}
