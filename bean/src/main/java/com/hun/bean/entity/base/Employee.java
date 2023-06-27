package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName employee_dev
 */
@TableName(value ="hun_base.employee_dev")
@Data
public class Employee implements Serializable {
    /**
     * 员工id
     */
    @TableId(type = IdType.AUTO)
    private Long employeeId;

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
     * 是否删除
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}