package com.abhinotes.m2o.connector.source.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class MQListener {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * This method gets the next message from the input queue and returns message
     * body as string
     *
     * @param queue
     * @return String - message body
     * @throws JMSException
     */
    public String getNextMessageFromQueue(String queue) {
        jmsTemplate.setReceiveTimeout(1000);
        Object msg = jmsTemplate.receiveAndConvert(queue);
        if (msg != null) {
            return msg.toString();
        } else {
            return null;
        }
    }
}