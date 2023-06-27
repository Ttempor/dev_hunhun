package com.hun.bean.dto.base.student;

import com.hun.bean.validation.annotation.Decimal;
import com.hun.bean.validation.annotation.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositStopGradeDto {
    @Id
    private Long studentId;
    @Decimal
    private BigDecimal balance;
    @Decimal
    private Long gradeId;
}
