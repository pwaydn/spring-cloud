package com.tiantian.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:
 */
@SpringBootApplication
@ComponentScan("com.tiantian")
@EnableEurekaClient
@EnableFeignClients("com.tiantian.feign")
public class FeignConsumerApplication19001 {
    public static void main(String[] args) {
        SpringApplication.run(FeignConsumerApplication19001.class,args);
    }
}
