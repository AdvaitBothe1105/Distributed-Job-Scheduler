package com.jobscheduler.api_gateway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RouteDebug {
    @Bean
    CommandLineRunner runner(Environment env) {
        return args -> {
            System.out.println(
                    env.getProperty("spring.cloud.gateway.mvc.routes[0].id")
            );
        };
    }
}
