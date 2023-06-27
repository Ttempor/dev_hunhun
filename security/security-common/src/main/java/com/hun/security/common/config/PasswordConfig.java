package com.hun.security.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 */
@Configuration
@Slf4j
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                throw new RuntimeException("不编码");
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                log.info("rawPassword:#{}", rawPassword);
                log.info("encodedPassword:#{}", encodedPassword);
                return encodedPassword.contentEquals(rawPassword);
            }

            @Override
            public boolean upgradeEncoding(String encodedPassword) {
                //不更新密码
                return false;
            }
        };
    }
}
