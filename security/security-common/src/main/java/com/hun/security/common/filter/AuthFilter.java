package com.hun.security.common.filter;

import com.hun.common.exception.BusinessException;
import com.hun.common.handler.HttpHandler;
import com.hun.security.common.adapter.AuthConfigAdapter;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.manager.TokenStore;
import com.hun.security.common.util.AuthUserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

/**
 * 授权过滤，只要实现AuthConfigAdapter接口，添加对应路径即可：
 */
@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private AuthConfigAdapter authConfigAdapter;

    @Autowired
    private HttpHandler httpHandler;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //请求的uri
        String requestUri = req.getRequestURI();

        //不需要授权的路径
        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();

        logger.info("excludePathPatterns:{}", excludePathPatterns);
        logger.info("requestUri:{}", requestUri);

        //匹配不需要授权的路径
        AntPathMatcher pathMatcher = new AntPathMatcher();


        // 如果匹配不需要授权的路径，就不需要校验是否需要授权
        if (!excludePathPatterns.isEmpty()) {
            for (String excludePathPattern : excludePathPatterns) {
                if (pathMatcher.match(excludePathPattern, requestUri)) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }
        //需要授权的路径,拿到token
        String accessToken = req.getHeader("Authorization");

        //用户信息
        UserInfoInTokenBO userInfoInToken = null;

        logger.info("token:{}", accessToken);
        try {
            // 如果有token，就要获取token
            if (StringUtils.hasText(accessToken)) {
                userInfoInToken = tokenStore.getUserInfoByAccessToken(accessToken, true);
            } else {
                throw new BusinessException("未登录");
            }
            // 保存上下文
            AuthUserContext.set(userInfoInToken);

            chain.doFilter(req, resp);

        }catch (Exception e) {
            // 手动捕获下非controller异常
            httpHandler.printServerResponseToWeb(e);
        } finally {
            //情况防止内存溢出
            AuthUserContext.clean();
        }
    }
}
