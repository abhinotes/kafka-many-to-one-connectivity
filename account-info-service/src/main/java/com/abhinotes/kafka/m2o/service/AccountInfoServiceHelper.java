package com.abhinotes.kafka.m2o.service;

import com.abhinotes.m2o.commons.entity.Account;
import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
import com.abhinotes.m2o.commons.entity.ServiceResponse;
import com.abhinotes.kafka.m2o.service.kafka.client.M2OProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountInfoServiceHelper {

    @Autowired
    M2OProducer m2OProducer;

    public Account getAccountInfo(String accountno) {
        return Account.builder().accountno(accountno)
                .customerName("Account Holder ".concat(accountno))
                .location("Mumbai").ccy("INR")
                .balance(100000.00).build();
    }

    public ServiceResponse getAccountInfoToKafkaTopic(String requestKey,
                                                      String accountno, String kafkatopic) {
        // Create a service response with account info result
        ServiceResponse serviceResponse = ServiceResponse.builder().result("Success")
                .responseObject(getAccountInfo(accountno)).build();

        //Publish results to Global result Response topic for
        // Kafka Streams to pic it up and perform join with request using requestKey
        m2OProducer.sendToTopic(kafkatopic, M2OMessageFormat.builder()
                .key(requestKey).serviceResponse(serviceResponse).build());

        return serviceResponse;
    }

}
