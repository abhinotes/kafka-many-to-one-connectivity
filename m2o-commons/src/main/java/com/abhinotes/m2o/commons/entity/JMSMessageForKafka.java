package com.abhinotes.m2o.commons.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class JMSMessageForKafka {
    private String key;
    private String source;
    private String timestamp;
    private String jmsqueue;
    private String jmsmessage;

    private SimpleDateFormat recieveTimeFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss Z", Locale.ENGLISH);

    JMSMessageForKafka(){

    }

    /**
     *
     * @param key : Kafka Message Key
     * @param source : Source of Message , Ex:- Environment Name
     * @param jmsqueue : Source MQ queue from where message has been read
     * @param jmsmessage : Actual JMS Message
     */
    public JMSMessageForKafka(String key, String source, String jmsqueue, String jmsmessage) {
        this.key = key;
        this.source = source;
        this.jmsqueue = jmsqueue;
        this.jmsmessage = jmsmessage;
        recieveTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.timestamp = recieveTimeFormat.format(new Date());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimestamp() {
        return timestamp;
    }



    public String getJmsqueue() {
        return jmsqueue;
    }

    public void setJmsqueue(String jmsqueue) {
        this.jmsqueue = jmsqueue;
    }

    public String getJmsmessage() {
        return jmsmessage;
    }

    public void setJmsmessage(String jmsmessage) {
        this.jmsmessage = jmsmessage;
    }

    @Override
    public String toString() {
        return "JMSMessageForKafka [key=" + key + ", source=" + source + ", timestamp=" + timestamp + ", jmsqueue="
                + jmsqueue + ", jmsmessage=" + jmsmessage + ", recieveTimeFormat=" + recieveTimeFormat + "]";
    }

}
