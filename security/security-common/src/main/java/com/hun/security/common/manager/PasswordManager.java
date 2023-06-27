/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.hun.security.common.manager;

import cn.hutool.crypto.symmetric.AES;
import com.hun.common.exception.BusinessException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Data
@Component
public class PasswordManager {
    private static final Logger logger = LoggerFactory.getLogger(PasswordManager.class);

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.password.signKey}")
    public String passwordSignKey;

   /* public String decryptPassword(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(passwordSignKey.getBytes(StandardCharsets.UTF_8), "AES"));
            String decryptStr = new String(cipher.doFinal(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8))));
            return decryptStr.substring(13);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new BusinessException("密码错误");
        }
    }*/

    public String decryptPassword(String data) {
        AES aes = new AES(passwordSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptPassword;
        try {
            decryptStr = String.valueOf(aes.encryptHex(data));
            decryptPassword = decryptStr.substring(13);
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw new BusinessException("密码错误");
        }
        return decryptPassword;
    }
}
