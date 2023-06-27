package com.hun.bean.entity.expand;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName grade_info
 */
@TableName(value ="hun_expand.grade_info")
@Data
public class GradeInfo implements Serializable {

    /**
     * 学生id
     */
    @TableId(type = IdType.AUTO)
    private Long gradeInfoId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生名
     */
    private String studentName;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 班级id
     */
    private Long gradeId;

    /**
     * 班级名
     */
    private String gradeName;

    /**
     * 老师id
     */
    private Long teacherId;

    /**
     * 老师姓名
     */
    private String employeeName;

    /**
     * 收费方式id
     */
    private Long chargeMethodId;

    /**
     * 收费方式名
     */
    private String chargeMethodName;

    /**
     * 收费表达式
     */
    private BigDecimal chargeMethodExpression;

    /**
     * 消费课时
     */
    private Integer consumePeriod;

    /**
     * 缺勤课时
     */
    private Integer missPeriod;

    /**
     * 总课时
     */
    private Integer totalPeriod;

    /**
     * 消费金额
     */
    private BigDecimal spendBalance;

    /**
     * 锁定余额
     */
    private BigDecimal lockBalance;

    /**
     * 锁定余额-消费金额
     */
    private BigDecimal lockSpendBalance;

    /**
     * 班级状态
     */
    private Integer gradeState;


    /**
     * 结课时间
     */
    private Long endTimestamp;

    /**
     * 结课时间
     */
    private LocalDateTime endDatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}