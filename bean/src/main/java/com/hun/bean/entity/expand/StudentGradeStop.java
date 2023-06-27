package com.hun.bean.entity.expand;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName student_grade_stop
 */
@TableName(value ="hun_expand.student_grade_stop")
@Data
public class StudentGradeStop implements Serializable {
    /**
     * 停课id
     */
    @TableId(type = IdType.AUTO)
    private Long stopId;

    /**
     * 
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
     * 停课时的班级状态
     */
    private Integer gradeState;

    /**
     * 停课时间
     */
    private LocalDateTime stopDatetime;

    /**
     * 停课时间
     */
    private Long stopTimestamp;

    /**
     * 停课原因
     */
    private String stopReason;

    /**
     * 充值余额
     */
    private BigDecimal depositLockBalance;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}