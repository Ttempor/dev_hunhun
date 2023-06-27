package com.hun.common.exception;

/**
 * 规范性校验异常
 */
public class BaseNormativeVerifyException extends RuntimeException {

    static final long serialVersionUID = -7034892266939L;
    public BaseNormativeVerifyException(String message) {
        super(message);
    }
}
