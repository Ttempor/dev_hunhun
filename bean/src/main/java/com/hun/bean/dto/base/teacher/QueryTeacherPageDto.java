package com.hun.bean.dto.base.teacher;

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
public class QueryTeacherPageDto {
    //页码
    @Page
    private Long p;

    @Nullable
    @Min(value = 1,message = "老师id最小为1")
    @Max(value = 10000,message = "老师id最大为10000")
    private Integer teacherId;

    @Nullable
    @Min(value = 1,message = "员工id最小为1")
    @Max(value = 10000,message = "员工id最大为10000")
    private Integer employeeId;

    @Nullable
    @Length(min = 1, max = 10, message = "老师姓名最短为1, 最长为10")
    private String employeeName;
}
