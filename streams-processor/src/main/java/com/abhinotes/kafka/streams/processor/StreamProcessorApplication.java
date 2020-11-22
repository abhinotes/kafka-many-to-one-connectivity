package com.abhinotes.kafka.streams.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableKafkaStreams
@EnableAutoConfiguration
public class StreamProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamProcessorApplication.class,args);
    }

    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
        }
    }
}
