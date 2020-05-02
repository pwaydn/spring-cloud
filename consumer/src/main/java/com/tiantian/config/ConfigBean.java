package com.tiantian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * description:
 */
@Configuration
public class ConfigBean {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
