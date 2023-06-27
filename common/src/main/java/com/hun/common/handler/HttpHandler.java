package com.hun.common.handler;

import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hun.common.exception.BusinessException;
import com.hun.common.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

@Component
public class HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    public <T> void printServerResponseToWeb(ExceptionResponse serverResponseEntity) {
        if (serverResponseEntity == null) {
            logger.info("print obj is null");
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            logger.error("requestAttributes is null, can not print to web");
            return;
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            logger.error("httpServletResponse is null, can not print to web");
            return;
        }
        logger.error("response error: " + serverResponseEntity.getMessage());
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(objectMapper.writeValueAsString(serverResponseEntity));
            printWriter.flush();
        }
        catch (IOException e) {
            throw new BusinessException("io 异常");
        }
    }

    public <T> void printServerResponseToWeb(Exception e) {
        if (e == null) {
            logger.info("print obj is null");
            return;
        }
        if (e instanceof  BusinessException) {
            printServerResponseToWeb(ExceptionResponse.businessException(e.getMessage()));
        } else {
            printServerResponseToWeb(ExceptionResponse.businessException(e.getMessage()));
        }
    }
}
