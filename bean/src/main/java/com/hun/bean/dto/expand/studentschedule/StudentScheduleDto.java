package com.hun.bean.dto.expand.studentschedule;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
public class StudentScheduleDto {
    @Id
    private Long gradeId;
    @Id
    private Long studentId;
    @Length(min = 5, max = 255, message = "最少5个字")
    @NotBlank
    private String stopReason;

}

