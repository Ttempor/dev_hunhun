package com.hun.bean.dto.expand.recordgradedrop;

import com.hun.bean.validation.annotation.Page;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * 条件分页查询
 */
@Data
public class QueryDropGradePageDto {
    //页码
    @Page
    private Long p;

    @Nullable
    @Length(min = 1, max = 30, message = "班级名最短为1, 最长为30")
    private String gradeName;

    @Nullable
    @Length(min = 1, max = 30, message = "老师名最短为1, 最长为10")
    private String employeeName;

    @Nullable
    @Length(min = 1, max = 30, message = "学生名最短为1, 最长为10")
    private String studentName;
}
