package com.hun.bean.app;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeScheduleBO {
    private Long scheduleId;
    private Integer period;
    private BigDecimal price;
    private Integer nowNumber;
    private Integer maxNumber;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String gradeRoom;
}
