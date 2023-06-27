package com.hun.bean.dto.base.grade;


import com.hun.bean.validation.annotation.Id;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BuyScheduleDto {
    @NotEmpty
    List<Long> scheduleIds;
    @Id
    private Long gradeId;
    @Id
    private Long studentId;
}
