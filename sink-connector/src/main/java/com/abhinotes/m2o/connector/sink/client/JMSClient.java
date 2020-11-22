package com.abhinotes.m2o.connector.sink.client;

import com.abhinotes.m2o.connector.exception.SinkConnectorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


@Component
public class JMSClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMSClient.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * @param jmsDestnation
     * @param message
     * @throws SinkConnectorRuntimeException
     */
    @Async("mRouterSinkExecutor")
    public void send(String jmsDestnation, String message) {
        LOGGER.debug(String.format("Attempting to send message to queue [%s], message : [%s]", jmsDestnation, message));
        try {
            jmsTemplate.send(jmsDestnation, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(message);
                }
            });
        } catch (JmsException e) {
            throw new SinkConnectorRuntimeException(String.format(
                    "Error while sending message to queue [%s], error message [%s]", jmsDestnation, e.getMessage()), e);
        }
    }


}
