package com.abhinotes.m2o.commons.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class M2OMessageFormat {
    private String key;
    private String source;
    @Builder.Default private long timestamp = System.currentTimeMillis();
    private String jmsqueue;
    private String jmsmessage;
    private ServiceResponse serviceResponse;
}
