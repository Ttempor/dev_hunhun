package com.hun.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialAnalysisTeacherVO {
    //班级名
    private Long teacherId;
    //班级名
    private String employeeName;
    //班级名
    private String gradeName;
    //班级名
    private Long gradeId;
    //班级名
    private Long chargeMethodId;
    //学员人数
    private Integer studentCount;
    //退课人数
    private Integer stopGradeCount;
    //停课人数
    private Integer tuiKeCount;
    //出勤人次
    private Integer attendCount;

    //缺勤人次
    private Integer missCount;
    //总课消金额
    private BigDecimal courseCancelIncome;
}
