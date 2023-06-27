package com.hun.bean.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecordGradeDropVO implements Serializable {
    /**
     * 退课id
     */
    private Long recordGradeDropId;

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
     * 退课时剩余锁定余额
     */
    private BigDecimal lockSpendBalance;

    /**
     * 退课时间
     */
    private LocalDateTime dropDatetime;

    /**
     * 退课原因
     */
    private String dropReason;

    @Serial
    private static final long serialVersionUID = 1L;
}
