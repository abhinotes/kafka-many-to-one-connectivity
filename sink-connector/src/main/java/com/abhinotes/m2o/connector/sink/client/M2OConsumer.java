package com.abhinotes.m2o.connector.sink.client;


import com.abhinotes.m2o.commons.entity.JMSMessageForKafka;
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

    @Value("${m2o.sink.App}")
    private String app;

    @Value("${m2o.sink.topicbase}")
    private String sourceTopicBase;

    @Autowired
    private JMSClient jmsClient;


    @KafkaListener(topicPattern = "${m2o.sink.topicbase}${m2o.sink.App}")
    public void receive(@Payload JMSMessageForKafka payload, @Headers MessageHeaders headers) {
        StringBuilder destinationJMSQueue = new StringBuilder();
        destinationJMSQueue.append(payload.getJmsqueue());
        LOGGER.info(String.format("From Topic %s%s ,Environment : %s, To JMS Queue %s,Payload : {%s}", sourceTopicBase, app,payload.getSource(),destinationJMSQueue.toString(), payload.getJmsmessage()));
        jmsClient.send(destinationJMSQueue.toString(), payload.getJmsmessage());
    }

}
