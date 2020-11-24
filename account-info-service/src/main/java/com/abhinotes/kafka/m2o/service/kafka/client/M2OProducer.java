package com.abhinotes.kafka.m2o.service.kafka.client;

import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;

public class M2OProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(M2OProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendToTopic(String topic, M2OMessageFormat message) {
        LOGGER.debug(String.format(
                "[sendToTopic] Sending Message ={%s} to Topic {%s} with message key {%s}",
                message.toString(), topic, message.getKey()));
        kafkaTemplate.send(MessageBuilder.withPayload(message).setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, message.getKey()).build());
    }


}
