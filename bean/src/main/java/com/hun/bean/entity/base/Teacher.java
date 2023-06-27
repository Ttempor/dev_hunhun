package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName teacher_dev
 */
@TableName(value ="hun_base.teacher_dev")
@Data
public class Teacher implements Serializable {
    /**
     * 老师职位id
     */
    @TableId(type = IdType.AUTO)
    private Long teacherId;

    /**
     * 员工id
     */
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
     * 所教班级数量
     */
    private Integer gradeCount;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}