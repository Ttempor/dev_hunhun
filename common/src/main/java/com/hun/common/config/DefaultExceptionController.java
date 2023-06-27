package com.hun.common.config;

import com.hun.common.exception.BusinessException;
import com.hun.common.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@Order(9999)
@RestControllerAdvice
@Slf4j
public class DefaultExceptionController {

    /**
     * 处理业务异常 501
     * @param e e
     */
    @ExceptionHandler(BusinessException.class)
    public ExceptionResponse ex(BusinessException e) {
        return ExceptionResponse.businessException(e.getMessage());
    }

    /**
     * 处理未知异常 500未知异常
     * @param e e
     */
    @ExceptionHandler(Exception.class)
    public ExceptionResponse ex(Exception e) {
        log.info("{}", e);
        return ExceptionResponse.unknownException("未知异常:" + e);
    }
}
