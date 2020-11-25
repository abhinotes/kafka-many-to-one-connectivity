package com.abhinotes.m2o.commons.entity;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JMSQueueExtendedInfo {

    private String queueName;
    private String messageParserClass;
    private HashMap<String, String> properties;
}
