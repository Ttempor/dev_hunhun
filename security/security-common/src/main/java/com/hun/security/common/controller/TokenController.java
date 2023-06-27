package com.hun.security.common.controller;

import com.hun.common.response.BaseResponse;
import com.hun.security.common.bo.TokenInfoBO;
import com.hun.security.common.dto.RefreshTokenDTO;
import com.hun.security.common.manager.TokenStore;
import com.hun.security.common.vo.TokenInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 */
@RestController
public class TokenController {

    @Autowired
    private TokenStore tokenStore;


    @PostMapping("/api/token/refresh")
    public BaseResponse<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenInfoBO tokenInfoBO = tokenStore.refreshToken(refreshTokenDTO.getRefreshToken());
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());

        return BaseResponse.ok(tokenInfoVO);
    }

}
