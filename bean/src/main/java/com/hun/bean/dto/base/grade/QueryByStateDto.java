package com.hun.bean.dto.base.grade;

import lombok.Data;

@Data
public class QueryByStateDto {
    private Integer isWait;
    private Integer isRun;
    private Integer isIng;
    private Integer isEnd;
}
