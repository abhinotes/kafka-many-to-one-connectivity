package com.abhinotes.m2o.connector.source.controller;

import com.abhinotes.m2o.connector.source.client.MQListener;
import com.abhinotes.m2o.connector.source.config.M2OProducer;
import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import com.abhinotes.m2o.connector.source.parser.MessageParsingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MQRestController {
    @Autowired
    M2OProducer kafkaSender;

    @Value("${destination.topic}")
    private String destinationTopic;

    @Value("${message.source}")
    private String messageSource;

    @Autowired
    private MQListener mqListener;

    @GetMapping("jms/read")
    public String fetchJMS(@RequestParam(value = "queue", required = true) String queue)
            throws MessageParsingException {
        String message = mqListener.getNextMessageFromQueue(queue);
        String responseMessage = "Success";
        try {
            kafkaSender.sendMessageFromMQQueueToTopic(destinationTopic,
                    MessageParsingHelper.getJMSMessageForKafka(messageSource, queue, message));
        } catch (MessageParsingException e) {
            responseMessage = String.format("Request Failed to process with following Error : [{%s}]", e.getMessage());
        }
        return responseMessage;
    }
}

