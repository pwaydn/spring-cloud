package com.tiantian.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:
 */
@SpringBootApplication
@ComponentScan("com.tiantian")
@MapperScan("com.tiantian.mapper")
@EnableEurekaClient                 // 注册
public class FeignProviderApplication18081 {
    public static void main(String[] args) {
        SpringApplication.run(FeignProviderApplication18081.class,args);
    }
}
