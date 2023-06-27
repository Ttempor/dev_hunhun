package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * @TableName course_dev
 */
@TableName(value ="hun_base.course_dev")
@Data
public class Course implements Serializable {
    /**
     * 课程id
     */
    @TableId(type = IdType.AUTO)
    private Long courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 收费方式id
     */
    private Long chargeMethodId;

    /**
     * 收费方式名
     */
    private String chargeMethodName;

    /**
     * 总价
     */
    private BigDecimal price;

    /**
     * 收费方式表达式
     */
    private BigDecimal chargeMethodExpression;

    /**
     * 课程总课时
     */
    private Integer courseTotalPeriod;

    /**
     * 课程当前班级总数
     */
    private Integer courseNowGradeCount;

    /**
     * 课程当前老师总数
     */
    private Integer courseNowTeacherCount;

    /**
     * 课程当前年级总数
     */
    private Integer courseNowStudentCount;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}