package com.abhinotes.kafka.m2o.service.controller;

import com.abhinotes.kafka.m2o.service.AccountInfoServiceHelper;
import com.abhinotes.m2o.commons.entity.Account;
import com.abhinotes.m2o.commons.entity.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class AccountInfoService {

    @Autowired
    AccountInfoServiceHelper accountInfoServiceHelper;

    @GetMapping("/account/info")
    public Account getAccountInfo(@RequestParam (value="accountno", required = true) String accountno ){
        return accountInfoServiceHelper.getAccountInfo(accountno);
    }

    @GetMapping("/account/info/topic")
    public ServiceResponse getAccountInfoToTopic(@RequestParam (value="requestKey", required = true) String requestKey,
                                                 @RequestParam (value="accountno", required = true) String accountno ,
                                                 @RequestParam (value="kafkatopic", required = true) String kafkatopic
                                         ){
        return accountInfoServiceHelper.getAccountInfoToKafkaTopic(requestKey, accountno, kafkatopic);
    }
}

