package com.hun.api.sys;

import com.hun.security.common.manager.TokenStore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.hun", exclude = {})
@MapperScan("com.hun.service.mapper")
public class ApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ApiApplication.class, args);
        TokenStore bean = run.getBean(TokenStore.class);
        System.out.println(bean);
    }

}
