package com.hun.security.api.util;

import com.hun.security.api.model.HunUser;
import com.hun.security.common.bo.UserInfoInTokenBO;
import com.hun.security.common.util.AuthUserContext;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 */
@UtilityClass
public class SecurityUtils {

    private static final String USER_REQUEST = "/p/";

    /**
     * 获取用户
     */
    public HunUser getUser() {
        if (!((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getRequestURI().startsWith(USER_REQUEST)) {
            // 用户相关的请求，应该以/p开头！！！
            throw new RuntimeException("user.request.error");
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        HunUser yamiUser = new HunUser();
        yamiUser.setUserId(userInfoInTokenBO.getUserId());
        yamiUser.setBizUserId(userInfoInTokenBO.getBizUserId());
        yamiUser.setEnabled(userInfoInTokenBO.getEnabled());
        yamiUser.setShopId(userInfoInTokenBO.getShopId());
        yamiUser.setStationId(userInfoInTokenBO.getOtherId());
        return yamiUser;
    }
}
