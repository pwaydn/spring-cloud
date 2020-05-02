package com.tiantian.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:
 */
@SpringBootApplication
@ComponentScan("com.tiantian")
public class ConsumerApplication19001 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication19001.class,args);
    }
}
