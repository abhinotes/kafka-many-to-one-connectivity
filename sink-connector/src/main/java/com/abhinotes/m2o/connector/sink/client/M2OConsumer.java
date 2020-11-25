package com.abhinotes.m2o.connector.sink.client;


import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
import com.abhinotes.m2o.commons.entity.ServiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class M2OConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(M2OConsumer.class);

    @Value("${m2o.sink.destinationQueue}")
    private String destinationQueue;

    @Value("${m2o.sink.sourceTopic}")
    private String sourceTopic;

    @Autowired
    private JMSClient jmsClient;


    @KafkaListener(topicPattern = "${m2o.sink.sourceTopic}")
    public void receive(@Payload M2OMessageFormat payload, @Headers MessageHeaders headers) {
        StringBuilder destinationJMSQueue = new StringBuilder();
        destinationJMSQueue.append(destinationQueue);
        String jsonForServiceResponse = null;

        ServiceResponse serviceResponse = payload.getServiceResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            jsonForServiceResponse  = objectMapper.writeValueAsString(serviceResponse);
        } catch (JsonProcessingException e) {
            LOGGER.info(String.format("Error processing service response " +
                            "From Topic %s , To JMS Queue %s, [Error Message : %s]",
                    sourceTopic, destinationQueue, e.getMessage()));
        }

        LOGGER.info(String.format("From Topic %s , To JMS Queue %s,Payload : {%s}",
                sourceTopic, destinationQueue, jsonForServiceResponse));
        jmsClient.send(destinationQueue, jsonForServiceResponse);
    }

}
