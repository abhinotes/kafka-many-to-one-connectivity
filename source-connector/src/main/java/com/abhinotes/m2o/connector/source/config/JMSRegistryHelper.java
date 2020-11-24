package com.abhinotes.m2o.connector.source.config;

import com.abhinotes.m2o.commons.constants.ConfigurationConstants;
import com.abhinotes.m2o.connector.source.client.M2OProducer;
import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import com.abhinotes.m2o.connector.source.parser.MessageParsingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.MessageListenerContainer;

import javax.jms.JMSException;

@Configuration
public class JMSRegistryHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSRegistryHelper.class);

    @Autowired
    private M2OProducer kafkaSender;

    @Value("${destination.topic}")
    private String destinationTopic;

    @Value("${message.source}")
    private String messageSource;

    @Autowired
    private ApplicationContext context;

    /**
     * This will return a Simple JMS Listener End point object for the provided queueName
     * Usage : This end point can be registered with JMS Listener End point registry.
     * This End point also has implementation to push received message to a KAFKA Topic defined as ${destination.topic} and using source environment identifier as ${message.source}
     * @param queueName
     * @return
     */
    public SimpleJmsListenerEndpoint getEndPoint(String queueName) {

        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(ConfigurationConstants.JMSREGISTRYPREFIX.concat(queueName));
        endpoint.setDestination(queueName);
        endpoint.setMessageListener(message -> {
            try {
                String messagePayload = message.getBody(String.class);
                LOGGER.debug(String.format("Message on Queue {%s} is {%s}>>>>", queueName, messagePayload));
                kafkaSender.sendMessageFromMQQueueToTopic(destinationTopic,
                        MessageParsingHelper.getM2OMessageFormat(messageSource, queueName, messagePayload));
            } catch (JMSException e) {
                LOGGER.error(String.format("Error reading mesaage on queue {%s}", queueName));
            } catch (MessageParsingException e) {
                LOGGER.error(String.format("Error parsing recieved message from queue {%s}, Error : %s", queueName,
                        e.getMessage()));
            }
        });
        return endpoint;
    }

    /**
     * JmsListenerEndpointRegistry object for bean : org.springframework.jms.config.internalJmsListenerEndpointRegistry
     * @return
     */
    public JmsListenerEndpointRegistry jmsRegistry() {
        return context.getBean("org.springframework.jms.config.internalJmsListenerEndpointRegistry",
                JmsListenerEndpointRegistry.class);
    }

    /**
     * Gets listener container for the provided Queue's end point
     * This object has methods to start, stop or de-register an end point
     * @param queue
     * @return
     */
    public MessageListenerContainer jmsListenerContainer(String queue) {
        return jmsRegistry().getListenerContainer(ConfigurationConstants.JMSREGISTRYPREFIX.concat(queue));
    }

}
