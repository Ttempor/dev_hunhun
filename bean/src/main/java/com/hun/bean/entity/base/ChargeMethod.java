package com.hun.bean.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName charge_method_dev
 */
@TableName(value ="hun_base.charge_method_dev")
@Data
public class ChargeMethod implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long chargeMethodId;

    /**
     * 
     */
    private String chargeMethodName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}