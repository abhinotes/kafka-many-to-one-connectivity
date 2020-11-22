package com.abhinotes.m2o.connector.source.parser;

import com.abhinotes.m2o.commons.constants.JMSQueueExtendedInfoConstants;
import com.abhinotes.m2o.commons.entity.JMSMessageForKafka;
import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import com.abhinotes.m2o.connector.source.exception.SourceConnectorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MessageParsingHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParsingHelper.class);

    private MessageParsingHelper() {
        throw new IllegalStateException("Utility class to Load MessageParserProperties!!");
    }

    public static Properties prop;

    static {
        InputStream is = null;
        String propFile = "/"+ System.getProperty("message.source")+"MessageQueueParser.properties";
        LOGGER.info(String.format("Trying loading property file %s ", propFile));
        prop = new Properties();
        is = ClassLoader.class.getResourceAsStream(propFile);
        try {
            prop.load(is);
        } catch (IOException e) {
            throw new SourceConnectorRuntimeException("Unable to load propery from MessageQueueParser.properties", e);
        }
        LOGGER.info(String.format("Loaded properties from MessageQueueParser.properties [{%s}] ", prop.toString()));
    }

    public static String getPropertyValue(String key) throws MessageParsingException {
        String queueProperty = prop.getProperty(key);
        if (queueProperty == null) {
            throw new MessageParsingException(String.format("No Property found with key {%s} ", key));
        }
        return queueProperty;
    }

    public static String[] getQueueProperties(String queue) throws MessageParsingException {
        return getPropertyValue(queue).split(",");
    }

    public static Map<String, String> getParameterKeyMap(String queue) throws MessageParsingException {

        HashMap<String, String> keysMap = new HashMap<>();
        String[] queueProp = getQueueProperties(queue);
        if (queueProp == null || queueProp.length == 0) {
            throw new MessageParsingException("Error while fetching JMS Queue {%s} parsing properties");
        }
        if (queueProp.length > 1) {
            for (int i = 1; i < queueProp.length; i++) {
                LOGGER.debug(String.format("Parser Key {%s} : {%s}", i, queueProp[i]));
                setKeyToMap(keysMap, queueProp[i]);
            }
        }
        return keysMap;
    }

    public static void setKeyToMap(Map<String, String> map, String key) throws MessageParsingException {
        String[] keyValue = key.split("=");
        if (keyValue != null && keyValue.length == 2) {
            map.put(keyValue[0], keyValue[1]);
        } else {
            throw new MessageParsingException("Error while populating Message Parser Key/Value Propertires ");
        }
    }

    public static JMSMessageForKafka getJMSMessageForKafka(String messageSource, String queue, String message)
            throws MessageParsingException {
        JMSMessageForKafka jmsMessageForKafka = null;
        String messageKey = null;
        String parsingClass = getQueueProperties(queue)[0];
        try {

            Class<?> messageParser = Class.forName(parsingClass);
            Method method = messageParser.getDeclaredMethod(JMSQueueExtendedInfoConstants.GET_MESSAGE_KEY, String.class, HashMap.class);
            method.setAccessible(true);
            messageKey = (String) method.invoke(messageParser.newInstance(),message, getParameterKeyMap(queue));
            jmsMessageForKafka = new JMSMessageForKafka(messageKey, messageSource, queue, message);

        } catch (ClassNotFoundException e) {
            throw new MessageParsingException(String.format(
                    "Message parsing class configuration is not correct. Provided Queue,Class name [{%s},{%s}]", queue,
                    parsingClass), e);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException e) {
            throw new MessageParsingException(
                    "Error while parsing message, please chekc if your Parser class uses JMSMessageParser interface class!!",e);
        } catch (SecurityException e) {
            throw new MessageParsingException(
                    "Security Exception while executing parsing method , Please follow exception stacktrace for more details!!",e);
        } catch (InvocationTargetException ite) {
            throw new MessageParsingException(
                    String.format("Method thrown an exception : {%s}",ite.getCause().getMessage()),ite);
        } catch (InstantiationException ie) {
            throw new MessageParsingException(
                    String.format("Unable to get an Instance of Parsing Class : {%s}",parsingClass),ie);
        }
        return jmsMessageForKafka;
    }

}
