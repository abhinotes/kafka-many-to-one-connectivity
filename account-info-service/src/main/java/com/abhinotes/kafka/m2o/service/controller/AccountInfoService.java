package com.abhinotes.kafka.m2o.service.controller;

import com.abhinotes.m2o.commons.entity.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class AccountInfoService {

    @GetMapping("/account/info")
    public Account getAccountInfo(@RequestParam (value="accountno", required = true) String accountno ){
        Account account = Account.builder().accountno(accountno)
                .customerName("Account Holder".concat(accountno))
                .location("Mumbai").ccy("INR")
                .balance(100000.00).build();
        return account;
    }
}
