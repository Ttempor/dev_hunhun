package com.hun.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BuyScheduleVO {
    /**
     * 课表id
     */
    private Long scheduleId;
    /**
     * 教室
     */
    private String gradeRoom;
    /**
     * 上课开始时间
     */
    private LocalDateTime startDatetime;
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

    /**
     * 是否购买了该课
     */
    private Boolean isBuy;

}
