package com.hun.bean.dto.base.student;

import com.hun.bean.validation.annotation.Page;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 条件分页查询
 */
@Data
public class QueryStudentPageDto {
    //页码
    @Page
    private Long p;

    @Nullable
    @Min(value = 1,message = "学生id最小为1")
    @Max(value = 10000,message = "学生id最大为10000")
    private Long studentId;

    @Nullable
    @Length(min = 1, max = 10, message = "学生姓名最短为1, 最长为10")
    private String studentName;
}
