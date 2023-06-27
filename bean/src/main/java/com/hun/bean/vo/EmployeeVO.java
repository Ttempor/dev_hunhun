package com.hun.bean.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmployeeVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    private Integer employeeId;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 员工性别
     */
    private String employeeSex;

    /**
     * 员工电话
     */
    private String employeePhone;

    /**
     * 职位数量
     */
    private Integer postCount;

    /**
     * 密码
     */
    private String password;


    /**
     * 老师id
     */
    private Integer teacherId;

}