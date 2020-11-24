package com.abhinotes.m2o.commons.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Account {

    private String accountno;
    private String customerName;
    private String location;
    private String ccy;
    private Double balance;
    @Builder.Default private long queryTimestamp = System.currentTimeMillis();

}
