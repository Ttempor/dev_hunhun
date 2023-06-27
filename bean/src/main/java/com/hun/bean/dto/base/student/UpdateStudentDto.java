package com.hun.bean.dto.base.student;

import com.hun.bean.validation.annotation.Id;
import com.hun.bean.validation.annotation.Phone;
import com.hun.bean.validation.annotation.Sex;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
public class UpdateStudentDto {
    @Id
    private Long studentId;
    @Length(min = 1, max = 5, message = "学生姓名长度限制在1-5")
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;
    @Sex
    private String studentSex;
    @Phone
    private String studentPhone;
}
