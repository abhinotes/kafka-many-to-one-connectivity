package com.abhinotes.m2o.commons.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAdminServer
@EnableAutoConfiguration
public class SpringAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAdminApplication.class,args);
    }

}
