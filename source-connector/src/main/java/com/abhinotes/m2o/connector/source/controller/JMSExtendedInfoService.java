package com.abhinotes.m2o.connector.source.controller;

import com.abhinotes.m2o.commons.entity.JMSQueueExtendedInfo;
import com.abhinotes.m2o.connector.source.config.M2OProducer;
import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JMSExtendedInfoService {
    @Value("${mq.extended.info.topic}")
    private String mqExtendedInfoTopic;

    @Autowired
    M2OProducer kafkaSender;


    @PostMapping(path = "/kafka/queue/add")
    public JMSQueueExtendedInfo addQueueExtendedDetails(@RequestBody String queueExtendedInfo) throws MessageParsingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JMSQueueExtendedInfo qmExtInfo = null;
        try {
            qmExtInfo = objectMapper.readValue(queueExtendedInfo, JMSQueueExtendedInfo.class);
            if (qmExtInfo!= null) {
                //kafkaSender.sendToTopic(mqExtendedInfoTopic, qmExtInfo.getQueueName(), qmExtInfo.toString());
            }
        } catch (JsonMappingException e) {
            throw new MessageParsingException("Input json field mapping is not correct",e);
        } catch (JsonProcessingException e) {
            throw new MessageParsingException("Input json processing failed!!",e);
        }
        return qmExtInfo;
    }
}
