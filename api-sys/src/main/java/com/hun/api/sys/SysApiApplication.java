package com.hun.api.sys;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = "com.hun", exclude = {})
@SpringBootApplication(scanBasePackages = "com.hun", exclude = {})
@MapperScan("com.hun.service.mapper")
public class SysApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysApiApplication.class, args);

    }

}
