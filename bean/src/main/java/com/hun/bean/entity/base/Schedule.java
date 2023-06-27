package com.hun.bean.entity.base;

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
 * @TableName schedule
 */
@TableName(value ="hun_base.schedule")
@Data
public class Schedule implements Serializable {
    /**
     * 课表id
     */
    @TableId(type = IdType.AUTO)
    private Long scheduleId;

    /**
     * 班级id
     */
    private Long gradeId;

    /**
     * 班级名称
     */
    private String gradeName;

    /**
     * 老师id
     */
    private Long teacherId;

    /**
     * 班级老师
     */
    private String employeeName;

    /**
     * 教室
     */
    private String gradeRoom;

    /**
     * 开始时间戳
     */
    private Long startTimestamp;

    /**
     * 上课开始时间
     */
    private LocalDateTime startDatetime;

    /**
     * 结束时间戳
     */
    private Long endTimestamp;

    /**
     * 上课结束时间
     */
    private LocalDateTime endDatetime;

    /**
     * 消耗课时
     */
    private Integer consumePeriod;

    /**
     * 这节课的价钱
     */
    private BigDecimal price;

    /**
     * 这节课的预期收入
     */
    private BigDecimal income;

    /**
     *
     */
    private Long chargeMethodId;

    /**
     * 收费方式表达式
     */
    private BigDecimal chargeMethodExpression;

    /**
     * 课表状态
     */
    private Integer scheduleState;

    /**
     * 当前学生
     */
    private Integer gradeNowStudentCount;

    /**
     * 最大学生
     */
    private Integer gradeMaxStudentCount;



    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}