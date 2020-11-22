package com.abhinotes.m2o.connector.source.parser;

import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import java.util.HashMap;


public interface JMSMessageParser {

    /**
     * Interface to provide parsed message information to be used before publishing to a kafka Topic
     * @param message
     * @param extendedInfo
     * @return
     * @throws MessageParsingException
     */
    public String getMessageKey(String message,HashMap<String,String> extendedInfo) throws MessageParsingException;

}