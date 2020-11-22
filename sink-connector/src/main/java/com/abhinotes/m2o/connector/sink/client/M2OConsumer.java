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

public class M2OConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(M2OConsumer.class);

    @Value("${m2o.sink.environment}")
    private String environment;

    @Value("${m2o.source.topicbase}")
    private String sourceTopicBase;

    @Value("${m2o.sink.queue.staging}")
    private boolean isStagingQueueEnabled;

    @Value("${m2o.sink.queue.staging.suffix}")
    private String stagingSuffix;

    @Autowired
    private JMSClient jmsClient;


    @KafkaListener(topicPattern = "${m2o.source.topicbase}${m2o.sink.environment}")
    public void receive(@Payload JMSMessageForKafka payload, @Headers MessageHeaders headers) {
        StringBuilder destinationJMSQueue = new StringBuilder();
        destinationJMSQueue.append(payload.getJmsqueue());
        if(isStagingQueueEnabled) {
            destinationJMSQueue.append(stagingSuffix);
        }
        LOGGER.info(String.format("From Topic %s%s ,Environment : %s, To JMS Queue %s,Payload : {%s}", sourceTopicBase, environment,payload.getSource(),destinationJMSQueue.toString(), payload.getJmsmessage()));
        jmsClient.send(destinationJMSQueue.toString(), payload.getJmsmessage());

    }

}
