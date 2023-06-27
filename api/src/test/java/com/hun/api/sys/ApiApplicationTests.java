package com.hun.api.sys;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hun.common.util.RedisUtil;
import com.hun.common.util.WXUtil;
import com.hun.security.api.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class ApiApplicationTests {

    private static final String APPID = "wx24fc72de8aafe0ef";
    private static final String APPSECRET = "f41caba88d80ac86e40bbfeadc343e63";
    @Resource
    private LoginController loginController;
    @Test
    void contextLoads() {
        System.out.println(WXUtil.getAccessToken());
        Object token = RedisUtil.get("WX_ACCESS_TOKEN");
        System.out.println(token);
        System.out.println(WXUtil.getPhoneNumber(WXUtil.getAccessToken(),
                "f148249e6a19ae7cbeee47857a8972ec7f2365eb4a2ce7ecb21db67c895d17ac"));
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?appid="
                + APPID + "&secret="
                + APPSECRET + "&js_code=0c1mljml2QWAnb4VA0ol2NDACl0mljmK"
                + "&grant_type=authorization_code"), HashMap.class);
        System.out.println(map.get("openid"));
        System.out.println(map.get("session_key"));
    }
}
