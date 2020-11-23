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

    String accountno;
    String customerName;
    String location;
    String ccy;
    Double balance;
    @Builder.Default private long queryTimestamp = System.currentTimeMillis();

}
