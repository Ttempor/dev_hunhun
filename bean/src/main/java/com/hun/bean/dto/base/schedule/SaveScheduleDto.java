package com.hun.bean.dto.base.schedule;

import com.hun.bean.validation.annotation.Id;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveScheduleDto {
    @Id
    private Long gradeId;
    @NotBlank
    @Length(min = 1, max = 30, message = "请填写教室")
    private String gradeRoom;

    @NotNull(message = "开始时间不为空")
    @Min(value = 0, message = "结束最小时间错误")
    private Long startTimestamp;

    @NotNull(message = "结束时间不为空")
    @Min(value = 0, message = "结束最小时间错误")
    private Long endTimestamp;

    @Min(value = 0, message = "最少消费0课时")
    @Max(value = 100, message = "最大消费100课时")
    @NotNull(message = "消费课时不能为空")
    private Integer consumePeriod;
}
