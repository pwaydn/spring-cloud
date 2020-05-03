package com.tiantian.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:
 */
@SpringBootApplication
@ComponentScan("com.tiantian")
@EnableZuulProxy
public class ZuulApplication10002 {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication10002.class,args);
    }
}
