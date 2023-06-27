package com.hun.bean.dto.expand.gradeinfo;


import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteGradeInfoDto {
    @Id
    private Long studentId;
    @Id
    private Long gradeId;
    @NotBlank
    @Length(min = 5, max = 200, message = "退课原因长度在5-200")
    private String reason;
}
