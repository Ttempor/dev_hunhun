package com.hun.bean.app;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentScheduleVO {
    private Integer period;
    private Integer state;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String room;
    private String teacher;
}
