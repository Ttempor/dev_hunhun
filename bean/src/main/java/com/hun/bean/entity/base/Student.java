package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName student_dev
 */
@TableName(value ="hun_base.student_dev")
@Data
public class Student implements Serializable {
    /**
     * 学生id
     */
    @TableId(type = IdType.AUTO)
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学生性别
     */
    private String studentSex;

    /**
     * 学生电话
     */
    private String studentPhone;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 锁定余额
     */
    private BigDecimal lockBalance;


    /**
     * 总消费记录
     */
    private BigDecimal spend;

    /**
     * 学生报读班级总数
     */
    private Integer gradeCount;

    /**
     * 微信唯一id
     */
    private String openId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}