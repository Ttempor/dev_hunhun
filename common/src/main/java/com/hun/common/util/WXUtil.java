package com.hun.common.util;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wx-util")
public class WXUtil implements ApplicationContextAware {
    private static String WX_ACCESS_TOKEN_KEY;
    private static String APP_ID;
    private static String APP_SECRET;
    private static ObjectMapper objectMapper;

    public static Map<String,String> wxCode(String code) {
        try {
            Map<String,String> map = objectMapper.readValue(HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?appid="
                    + APP_ID + "&secret="
                    + APP_SECRET + "&js_code="
                    + code
                    + "&grant_type=authorization_code"), HashMap.class);
            log.info("{}", map);
            return map;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("异常");
        }
    }


    public static String getAccessToken() {
        String access_token = RedisUtil.get(WX_ACCESS_TOKEN_KEY);
        if (access_token != null) {
            return access_token;
        }
        try {
            Map<String, Object> map = objectMapper.readValue(HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?" +
                    "grant_type=client_credential&appid="
                    + APP_ID + "&secret="
                    + APP_SECRET), HashMap.class);
            log.info("{}", map);
            access_token = (String) map.get("access_token");
            Integer expires = (Integer) map.get("expires_in");
            RedisUtil.set(WX_ACCESS_TOKEN_KEY, access_token, expires - 60);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("异常");
        }
        return access_token;
    }

    public static String getPhoneNumber(String accessToken, String code) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", code);
            String json = objectMapper.writeValueAsString(map);
            map = objectMapper.readValue(HttpUtil.
                    post("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" +
                            accessToken, json), HashMap.class);
            log.info("{}", map);
            Map<String, Object> phoneInfo = (Map<String, Object>) map.get("phone_info");
            Object purePhoneNumber = phoneInfo.get("purePhoneNumber");
            return (String) purePhoneNumber;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("异常");
        }
    }
//
//    public static String wxDecrypt(String encrypted, String session_key, String iv) {
//        String result = null;
//        byte[] encrypted64 = Base64.decodeBase64(encrypted);
//        byte[] key64 = Base64.decodeBase64(session_key);
//        byte[] iv64 = Base64.decodeBase64(iv);
//        // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
//        int base = 16;
//        if (key64.length % base != 0) {
//            int groups = key64.length / base
//                    + (key64.length % base != 0 ? 1 : 0);
//            byte[] temp = new byte[groups * base];
//            Arrays.fill(temp, (byte) 0);
//            System.arraycopy(key64, 0, temp, 0, key64.length);
//            key64 = temp;
//        }
//        try {
//            init();
//            result = new String(decrypt(encrypted64, key64, generateIV(iv64)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//
//    /**
//     *    * 初始化密钥
//     *
//     */
//
//    public static void init() throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
//        KeyGenerator.getInstance(WxConstant.AES).init(128);
//    }
//
//    /**
//     *    * 生成iv
//     *
//     */
//    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
//        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
//        // Arrays.fill(iv, (byte) 0x00);
//        AlgorithmParameters params = AlgorithmParameters.getInstance(WxConstant.AES);
//        params.init(new IvParameterSpec(iv));
//        return params;
//    }
//
//    /**
//     *    * 生成解密
//     *
//     */
//    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv)
//            throws Exception {
//        Key key = new SecretKeySpec(keyBytes, WxConstant.AES);
//        Cipher cipher = Cipher.getInstance(WxConstant.AES_CBC_PADDING);
//        // 设置为解密模式
//        cipher.init(Cipher.DECRYPT_MODE, key, iv);
//        return cipher.doFinal(encryptedData);
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    public void setWxAccessTokenKey(String wxAccessTokenKey) {
        WX_ACCESS_TOKEN_KEY = wxAccessTokenKey;
    }

    public void setAppId(String appId) {
        APP_ID = appId;
    }

    public void setAppSecret(String appSecret) {
        APP_SECRET = appSecret;
    }
}
