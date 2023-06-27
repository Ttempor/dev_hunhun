package com.hun.bean.dto.expand.miss;

import com.hun.bean.validation.annotation.Id;
import com.hun.bean.validation.annotation.Page;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
public class InsertMissScheduleDto {
    @Id
    private Long gradeId;

    @Id
    private Long studentId;

    @NotBlank
    @Length(min = 5, max = 200, message = "缺课原因字数5-200")
    private String missReason;
}
