package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @TableName grade
 */
@TableName(value ="hun_base.grade")
@Data
public class Grade implements Serializable {
    /**
     * 班级id
     */
    @TableId(type = IdType.AUTO)
    private Long gradeId;

    /**
     * 班级名称
     */
    private String gradeName;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 老师id
     */
    private Long teacherId;

    /**
     * 老师名称
     */
    private String employeeName;

    /**
     * 当前班级人数
     */
    private Integer gradeNowStudentCount;

    /**
     * 最大班级认识
     */
    private Integer gradeMaxStudentCount;

    /**
     * 已上课时
     */
    private Integer gradeNowPeriod;

    /**
     * 总课时
     */
    private Integer gradeTotalPeriod;



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
     * 实时收入
     */
    private BigDecimal income;


    /**
     * 总价
     */
    private BigDecimal price;

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


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}