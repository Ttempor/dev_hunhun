package com.hun.bean.dto.base.teacher;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;

@Data
public class SaveTeacherDto {
    @Id
    private Long employeeId;
}
