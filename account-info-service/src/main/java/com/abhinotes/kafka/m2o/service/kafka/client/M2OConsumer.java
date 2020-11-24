package com.abhinotes.kafka.m2o.service.kafka.client;


import com.abhinotes.kafka.m2o.service.AccountInfoRequestHandler;
import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
import com.abhinotes.m2o.commons.entity.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class M2OConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(M2OConsumer.class);

    @Autowired
    AccountInfoRequestHandler requestHandler;

    @KafkaListener(topicPattern = "${m2o.kafka.requestTopic}")
    public void receive(@Payload M2OMessageFormat payload, @Headers MessageHeaders headers) {
        LOGGER.info(String.format("From Message Key %s , Kafka Topic %s ,  Calling service handler %s",
               headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY), headers.get(KafkaHeaders.RECEIVED_TOPIC),
                requestHandler.getClass().getName()));

        // Call to a RequestHandler implementation
        ServiceResponse serviceResponse = requestHandler
                .handleRequestOnKafkaTopic(payload.getJmsmessage(),
                        headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY).toString());
        LOGGER.info(serviceResponse.toString());
    }

}
