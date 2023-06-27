package com.hun.bean.app;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AppGradeVO {
    private String gradeName;//班级名
    private Long gradeId; //班级id
    private String employeeName;//老师名
    private Long teacherId;//老师id
    private BigDecimal price;//总价
    private Integer chargeType; //收费类型，套餐，课时
    private BigDecimal chargeExpression;//收费方式 1课时  20总价
    private Integer nowNumber; //班级当前人数
    private Integer maxNumber; //班级最大人数
    private List<GradeScheduleBO> schedules; //课时
}
