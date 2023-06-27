package com.hun.bean.dto.base.employee;

import com.hun.bean.validation.annotation.Id;
import com.hun.bean.validation.annotation.Phone;
import com.hun.bean.validation.annotation.Sex;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
public class UpdateEmployeeDto {
    @Id
    private Long employeeId;
    @Length(min = 1, max = 5, message = "员工姓名长度限制在1-5")
    @NotBlank(message = "姓名不能为空")
    private String employeeName;
    @Sex
    private String employeeSex;
    @Length(min = 6, max = 12, message = "要求密码长度在6-12个任意字符")
    @NotBlank(message = "密码不能为空")
    private String password;
    @Phone
    private String employeePhone;
}
