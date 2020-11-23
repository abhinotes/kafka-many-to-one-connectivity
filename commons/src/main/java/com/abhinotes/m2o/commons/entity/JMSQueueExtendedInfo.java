package com.abhinotes.m2o.commons.entity;

import java.util.HashMap;
import java.util.Map;

public class JMSQueueExtendedInfo {

    private String queueName;
    private String messageParserClass;
    private HashMap<String, String> properties;


    /**
     *
     * @param queueName : Name of the queue this extended message info belongs
     * @param messageParserClass : This class will handle message key parsing logic
     * @param properties : Set following properties
     * keyFromPosition : For Fixed length message, set this to start position of Message Key in source message
     * keyToPosition : For Fixed length message, set this to end position of Message Key in source message
     * xPath : For XML message format which is not an ESB Messages, set this to the xPath of the Message Key
     */
    public JMSQueueExtendedInfo(String queueName, String messageParserClass, Map<String, String> properties) {
        super();
        this.queueName = queueName;
        this.messageParserClass = messageParserClass;
        this.properties = (HashMap<String, String>) properties;
    }


    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMessageParserClass() {
        return messageParserClass;
    }

    public void setMessageParserClass(String messageParserClass) {
        this.messageParserClass = messageParserClass;
    }


    public Map<String, String> getProperties() {
        return properties;
    }


    public void setProperties(Map<String, String> properties) {
        this.properties = (HashMap<String, String>) properties;
    }

}
