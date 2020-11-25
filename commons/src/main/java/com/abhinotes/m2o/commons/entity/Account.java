package com.abhinotes.m2o.commons.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String accountno;
    private String customerName;
    private String location;
    private String ccy;
    private Double balance;
    @Builder.Default private long queryTimestamp = System.currentTimeMillis();

}
