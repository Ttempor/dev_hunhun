package com.hun.bean.dto.expand.recordspend;

import com.hun.bean.validation.annotation.Page;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
public class QueryRecordSpendDto {
    //查询的页码
    //查询的班级名
    //查询的学生名
    @Page
    private Long p;

    @Nullable
    @Length(min = 1, max = 30, message = "班级名最短为1, 最长为30")
    private String gradeName;

    @Nullable
    @Length(min = 1, max = 10, message = "学生名最短为1, 最长为10")
    private String studentName;

}
