package com.circuitguard.ai.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@ComponentScan(basePackages = {"com.circuitguard.ai.usermanagement", "com.skillrat.auth"})
public class UserManagement {

    public static void main(String[] args) {
        SpringApplication.run(UserManagement.class, args);
    }

}
