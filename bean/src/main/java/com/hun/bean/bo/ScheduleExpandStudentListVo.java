package com.hun.bean.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScheduleExpandStudentListVo {
    /**
     * 课号
     */
    private Long scheduleId;
    /**
     * 课号
     */
    private Long studentScheduleId;
    /**
     * 课号
     */
    private Integer scheduleState;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生名
     */
    private String studentName;

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
     * 是否缺勤
     */
    private Boolean isMiss;

    /**
     * 是否是调课来的
     */
    private Boolean isTiaoke;

}
