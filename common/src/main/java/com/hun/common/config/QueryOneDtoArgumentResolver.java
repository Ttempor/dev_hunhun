package com.hun.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
/**
 * 配置QueryOneDto的参数影视，把p映射到param，把m映射到method
 */
@Deprecated//过时,查询结构已经修改,不需要该解析器
public class QueryOneDtoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
   /* @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == QueryDto.class;
    }

    *//**
     * 解析参数，并检验
     *//*
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        QueryDto queryDto = new QueryDto();
        String m = webRequest.getParameter("m");
        if (!StringUtils.hasText(m) || m.length() > 20) {
            throw new QueryOneDtoArgumentResolverException("方式参数异常");
        }

        queryDto.setMethod(m);
        String p = webRequest.getParameter("p");
        if (!StringUtils.hasText(p) || p.length() > 20) {
            throw new QueryOneDtoArgumentResolverException("查询参数异常");
        }
        queryDto.setParam(p);
        return queryDto;
    }*/
}