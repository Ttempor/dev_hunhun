package com.hun.common.config;

import com.hun.common.exception.BaseNormativeVerifyException;
import com.hun.common.response.ExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;

@Order(0)
@RestControllerAdvice
public class DefaultValidationController {
    /**
     * 处理页码转换异常 502 规范性校验异常
     *
     * @param e e
     */
    @ExceptionHandler(BaseNormativeVerifyException.class)
    public ExceptionResponse ex(BaseNormativeVerifyException e) {
        return ExceptionResponse.normalVerifyException("基础校验未知异常:" + e.getMessage());
    }

    /**
     * 处理请求参数解析异常 502 规范性校验异常
     *
     * @param e e
     */
    @ExceptionHandler({ServletRequestBindingException.class, UnexpectedTypeException.class})
    public ExceptionResponse ex(Exception e) {
        e.printStackTrace();
        return ExceptionResponse.normalVerifyException("参数缺少");
    }


    /**
     * 处理请求参数解析异常 502 规范性校验异常
     *
     * @param e e
     */
    @ExceptionHandler({HttpMessageConversionException.class, IllegalStateException.class})
    public ExceptionResponse ex(RuntimeException e) {
        e.printStackTrace();
        return ExceptionResponse.normalVerifyException("参数格式错误");
    }


    /**
     * 502
     * 处理Validated校验异常
     * 注: 常见的ConstraintViolationException异常， 也属于ValidationException异常
     *
     * @param e 捕获到的异常
     * @return 返回给前端的data
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ExceptionResponse handleParameterVerificationException(Exception e) {
        String msg = null;
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
        } else if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
                if (msg != null && msg.length() > 30) {
                    e.printStackTrace();
                    msg = "参数类型错误,详情查看后端日志";
                }
            }
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            /// ValidationException 的其它子类异常
        } else {
            e.printStackTrace();
            msg = "有参数为空, 或者其他参数异常";
        }
        return ExceptionResponse.normalVerifyException(msg);
    }
}
