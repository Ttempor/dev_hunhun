package com.hun.bean.entity.expand;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName record_grade_drop
 */
@TableName(value ="hun_expand.record_grade_drop")
@Data
public class RecordGradeDrop implements Serializable {
    /**
     * 退课id
     */
    @TableId(type = IdType.AUTO)
    private Long recordGradeDropId;

    /**
     * 班级信息id
     */
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
     * 退课时消费课时
     */
    private Integer consumePeriod;

    /**
     * 退课时缺勤课时
     */
    private Integer missPeriod;

    /**
     * 退课时总课时
     */
    private Integer totalPeriod;

    /**
     * 退课时消费金额
     */
    private BigDecimal spendBalance;

    /**
     * 退课时锁定余额
     */
    private BigDecimal lockBalance;

    /**
     * 退课时锁定余额
     */
    private BigDecimal lockSpendBalance;

    /**
     * 退课时班级状态
     */
    private Integer gradeState;

    /**
     * 退课时间
     */
    private LocalDateTime dropDatetime;

    /**
     * 退课时间戳
     */
    private Long dropTimestamp;

    /**
     * 退课原因
     */
    private String dropReason;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}