package com.hun.bean.dto.base.grade;

import com.hun.bean.validation.annotation.Page;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

/**
 * 条件分页查询
 */
@Data
public class QueryGradePageDto {
    //页码
    @Page
    private Long p;

    @Nullable
    @Length(min = 1, max = 30, message = "班级名最短为1, 最长为30")
    private String gradeName;

    @Nullable
    @Length(min = 1, max = 30, message = "课程名最短为1, 最长为30")
    private String courseName;

    @Nullable
    @Length(min = 1, max = 30, message = "老师名最短为1, 最长为30")
    private String employeeName;

    @Nullable
    private Integer isWait;
    @Nullable
    private Integer isRun;
    @Nullable
    private Integer isIng;
    @Nullable
    private Integer isEnd;
}
