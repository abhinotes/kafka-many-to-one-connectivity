package com.abhinotes.m2o.connector.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SourceConnectorApplication {
    @Value("${pool.size}")
    private int poolSize=10;

    @Value("${maxpool.size}")
    private int maxPoolSize=50;

    public static void main(String[] args) {
        SpringApplication.run(SourceConnectorApplication.class, args);
    }

    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
        }
    }

    @Bean(name = "mRouterSourceExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor mrouterExecutor = new ThreadPoolTaskExecutor();
        mrouterExecutor.setCorePoolSize(poolSize);
        mrouterExecutor.setMaxPoolSize(maxPoolSize);
        mrouterExecutor.setThreadNamePrefix("m2OSourceConnector-");
        mrouterExecutor.initialize();
        return mrouterExecutor;
    }

}
