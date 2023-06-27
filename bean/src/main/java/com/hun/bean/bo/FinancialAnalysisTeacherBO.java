package com.hun.bean.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialAnalysisTeacherBO {
    //出勤人数
    private Integer attendCount;
    //缺勤人数
    private Integer missCount;
    //课消总收入包括套餐
    private BigDecimal courseCancelIncome;

}
