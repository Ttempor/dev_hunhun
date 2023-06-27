package com.hun.bean.dto;

import lombok.Data;

/**
 * 查询一个method=id
 * 差点多个method=xx
 * param=查询的参数
 */
@Data
public class QueryDto {
    /**
     * 在参数解析中校验数据
     */
    private String method;
    /**
     * 在参数解析中校验数据
     */
    private String param;
}
