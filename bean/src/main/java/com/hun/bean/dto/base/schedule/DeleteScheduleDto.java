package com.hun.bean.dto.base.schedule;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
@Data
public class DeleteScheduleDto {

    @Id
    private Long scheduleId;
    @Id
    private Long gradeId;
}
