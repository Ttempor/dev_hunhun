package com.hun.security.common.config;

import com.hun.common.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;


@Order(0)
@RestControllerAdvice
@Slf4j
public class AccessExceptionController {
    /**
     * 处理未知异常 500未知异常
     * @param e e
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionResponse ex(Exception e) {
        return ExceptionResponse.unknownException("无权限");
    }
}
