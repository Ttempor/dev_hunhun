package com.hun.common.response;

import lombok.Data;

@Data
public class ExceptionResponse {
    private Integer code;
    private String message;

    private ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //未知异常
    public static ExceptionResponse unknownException(String message) {
        return new ExceptionResponse(500, message);
    }
    //业务异常
    public static ExceptionResponse businessException(String message) {
        return new ExceptionResponse(501, message);
    }
    //规范性校验异常
    public static ExceptionResponse normalVerifyException(String message) {
        return new ExceptionResponse(502, message);
    }
}
