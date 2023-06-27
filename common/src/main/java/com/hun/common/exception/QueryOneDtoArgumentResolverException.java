package com.hun.common.exception;

import com.hun.common.exception.BaseNormativeVerifyException;

/**
 * 规范性校验异常
 * 查询一个对象的dto参数解析异常
 */
public class QueryOneDtoArgumentResolverException extends BaseNormativeVerifyException {
    static final long serialVersionUID = -703425766939L;
    public QueryOneDtoArgumentResolverException(String message) {
        super(message);
    }
}
