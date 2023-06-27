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
 * @TableName student_schedule
 */
@TableName(value ="hun_expand.student_schedule")
@Data
public class StudentSchedule implements Serializable {
    /**
     * 学生_排课id
     */
    @TableId(type = IdType.AUTO)
    private Long studentScheduleId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 课表id
     */
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
     * 该节课价格
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
     * 调班id
     */
    private Long sourceScheduleId;

    /**
     * 调班id
     */
    private Long sourceGradeId;
    /**
     * 调班id
     */
    private String missReason;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}