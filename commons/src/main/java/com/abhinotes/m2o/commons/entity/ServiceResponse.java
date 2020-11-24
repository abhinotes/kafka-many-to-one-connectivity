package com.abhinotes.m2o.commons.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class ServiceResponse {
    @Builder.Default private String refID = UUID.randomUUID().toString();
    private String result;
    private Object responseObject ;
}
