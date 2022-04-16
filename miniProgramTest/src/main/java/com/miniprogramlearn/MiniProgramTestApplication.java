package com.miniprogramlearn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.miniprogramlearn.mapper")
//@ComponentScan(basePackages = {"com.miniprogramlearn.service.imply","com.miniprogramlearn"})
@Import(com.miniprogramlearn.config.RestTemplateConfig.class)
@EnableTransactionManagement
public class MiniProgramTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniProgramTestApplication.class, args);
    }
}



