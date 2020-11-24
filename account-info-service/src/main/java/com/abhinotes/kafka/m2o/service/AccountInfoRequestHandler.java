package com.abhinotes.kafka.m2o.service;

import com.abhinotes.kafka.m2o.service.exception.AccountInfoServiceException;
import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
import com.abhinotes.m2o.commons.entity.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class AccountInfoRequestHandler  {

    @Autowired
    AccountInfoServiceHelper accountInfoServiceHelper;

    @Value("${m2o.account.service.reply.topic}")
    private String replyTopic;

    public ServiceResponse handleRequestOnKafkaTopic(String payload, String key) {
        ServiceResponse serviceResponse  = null;
        try {
            serviceResponse = accountInfoServiceHelper.getAccountInfoToKafkaTopic(
                    key,
                     fetchAccountNo(payload),
                    replyTopic);
        } catch (AccountInfoServiceException e) {
            serviceResponse  = ServiceResponse.builder()
                    .result("Failed").refID(key)
                    .responseObject(e.getMessage()).build();
        }
        return serviceResponse;
    }

    private String fetchAccountNo(String requestMessage) throws AccountInfoServiceException {
        if(requestMessage == null || requestMessage.length() < 33 ) {
            throw new AccountInfoServiceException(
                    String.format("Invalid request message received : %s", requestMessage));
        }
        return requestMessage.substring(4,15);
    }
}
