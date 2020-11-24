package com.abhinotes.m2o.commons.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@Builder
@ToString
public class JMSQueueExtendedInfo {

    private String queueName;
    private String messageParserClass;
    private HashMap<String, String> properties;
}
