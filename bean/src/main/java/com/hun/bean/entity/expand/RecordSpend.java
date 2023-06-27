package com.hun.bean.entity.expand;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName record_spend
 */
@TableName(value ="hun_expand.record_spend")
@Data
public class RecordSpend implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long recordSpendId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 班级id
     */
    private Long gradeId;

    /**
     * 班级名
     */
    private String gradeName;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 消费时间
     */
    private LocalDateTime spendTime;

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
     * 收费方式表达式
     */
    private BigDecimal chargeMethodExpression;

    /**
     * 消费金额
     */
    private BigDecimal consume;

    /**
     * 余额
     */
    private BigDecimal balance;


    /**
     * 描述
     */
    private String description;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}