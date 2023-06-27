package com.hun.bean.dto.base.employee;

import com.hun.bean.validation.annotation.Phone;
import com.hun.bean.validation.annotation.Sex;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SaveEmployeeDto {
    @Length(min = 2, max = 3, message = "员工姓名长度限制在2-3")
    @NotBlank(message = "姓名不能为空")
    private String employeeName;
    @Sex
    private String employeeSex;
    @Phone
    private String employeePhone;
}
