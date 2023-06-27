package com.hun.security.common.controller;

import cn.hutool.core.util.StrUtil;
import com.hun.common.response.BaseResponse;
import com.hun.security.common.manager.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 */
@RestController
public class LogoutController {

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/api/logOut")
    public BaseResponse<Void> logOut(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            return BaseResponse.ok();
        }
        // 删除该用户在该系统当前的token
        tokenStore.deleteCurrentToken(accessToken);
        return BaseResponse.ok();
    }
}
