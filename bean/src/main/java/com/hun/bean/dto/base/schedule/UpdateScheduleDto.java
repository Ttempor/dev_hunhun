package com.hun.bean.dto.base.schedule;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UpdateScheduleDto {
    @Id
    private Long scheduleId;

    @NotBlank(message = "教室名不能为空")
    @Length(min = 1, max = 30, message = "教室长度错误")
    private String gradeRoom;

    @NotNull(message = "请选择开始时间")
    @Min(value = 0, message = "结束最小时间错误")
    private Long startTimestamp;

    @NotNull(message = "请选择结束时间")
    @Min(value = 0, message = "结束最小时间错误")
    private Long endTimestamp;
}
