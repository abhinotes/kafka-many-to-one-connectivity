package com.abhinotes.m2o.connector.sink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableAutoConfiguration
@EnableKafka
@EnableJms
public class SinkConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinkConnectorApplication.class,args);
    }
}
