package com.hun.bean.dto.base.course;

import com.hun.bean.validation.annotation.Decimal;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SaveCourseDto {
    @Length(min = 1, max = 30, message = "课程名长度在1-30")
    @NotBlank(message = "课程名不能为空")
    private String courseName;
    @Min(value = 1, message = "收费方式id最小为1, 最大为999")
    @Max(value = 999, message = "收费方式id最小为1, 最大为999")
    @NotNull
    private Long chargeMethodId;
    @Decimal
    private BigDecimal chargeMethodExpression;
    @Min(value = 1, message = "课时最小为1, 最大为999")
    @Max(value = 999, message = "课时最小为1, 最大为999")
    @NotNull
    private Integer courseTotalPeriod;
}
