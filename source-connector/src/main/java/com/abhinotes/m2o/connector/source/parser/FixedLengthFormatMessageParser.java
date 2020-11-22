package com.abhinotes.m2o.connector.source.parser;

import com.abhinotes.m2o.commons.constants.JMSQueueExtendedInfoConstants;
import com.abhinotes.m2o.connector.source.exception.MessageParsingException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FixedLengthFormatMessageParser implements JMSMessageParser {


    @Override
    public String getMessageKey(String message, HashMap<String, String> extendedInfo) throws MessageParsingException {
        String messageKey = null;
        String fromPosition = extendedInfo.get(JMSQueueExtendedInfoConstants.KEY_FROM_POSITION);
        String toPosition = extendedInfo.get(JMSQueueExtendedInfoConstants.KET_TO_POSITION);
        try {
            messageKey = message.substring(Integer.parseInt(fromPosition), Integer.parseInt(toPosition));
        } catch (NumberFormatException nfe) {
            throw new MessageParsingException(
                    String.format("Error prosessing Message key value in fixed length message, please check your start,end position configuration. Provided : [From {%s},To {%s}]",fromPosition,toPosition),
                    nfe);
        }
        return messageKey;
    }

}