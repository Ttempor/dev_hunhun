package com.hun.bean.dto.base.grade;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateGradeDto {
    @Id
    private Long gradeId;
    @NotBlank(message = "班级名不能为空")
    @Length(min = 1, max = 30, message = "班级名称长度最长为30")
    private String gradeName;
    @NotNull
    @Min(value = 1, message = "班级最小人数为1, 最大人数为99")
    @Max(value = 99, message = "班级最小人数为1, 最大人数为99")
    private Integer gradeMaxStudentCount;
}
