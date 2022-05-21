package me.jimmy.userservice.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import me.jimmy.userservice.error.FeignErrorDecoder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
//
//    @Bean
//    public ErrorDecoder feignErrorDecoder() {
//        return new FeignErrorDecoder();
//    }
}
