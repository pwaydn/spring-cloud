package com.tiantian.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:
 */
@SpringBootApplication
@ComponentScan("com.tiantian")
@EnableEurekaServer
public class EurekasServer17003 {
    public static void main(String[] args) {
        SpringApplication.run(EurekasServer17003.class,args);
    }
}
