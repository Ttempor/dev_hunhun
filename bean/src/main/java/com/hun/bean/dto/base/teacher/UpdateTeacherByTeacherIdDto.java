package com.hun.bean.dto.base.teacher;

import com.hun.bean.validation.annotation.Id;
import com.hun.bean.validation.annotation.Phone;
import com.hun.bean.validation.annotation.Sex;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
public class UpdateTeacherByTeacherIdDto {
    @Id
    private Long teacherId;
    @Length(min = 1, max = 5, message = "员工姓名长度限制在1-5")
    @NotBlank(message = "姓名不能为空")
    private String employeeName;
    @Sex
    private String employeeSex;
    @Phone
    private String employeePhone;
}
