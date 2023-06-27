package com.hun.common.exception;

public class BusinessException extends RuntimeException{
    static final long serialVersionUID = -70344112366939L;
    public BusinessException(String message) {
        super(message);
    }
}
