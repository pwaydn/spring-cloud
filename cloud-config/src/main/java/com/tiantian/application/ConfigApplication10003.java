package com.tiantian.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * description:
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication10003 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication10003.class,args);
    }
}
