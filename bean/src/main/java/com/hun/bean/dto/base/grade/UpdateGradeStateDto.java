package com.hun.bean.dto.base.grade;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UpdateGradeStateDto {
    @Id
    private Long gradeId;
    @NotNull
    private Integer gradeState;
}
