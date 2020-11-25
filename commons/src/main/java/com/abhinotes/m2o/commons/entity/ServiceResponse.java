package com.abhinotes.m2o.commons.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    @Builder.Default private String refID = UUID.randomUUID().toString();
    private String result;
    private Object responseObject ;
}
