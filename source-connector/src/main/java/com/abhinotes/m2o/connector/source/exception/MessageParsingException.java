package com.abhinotes.m2o.connector.source.exception;

public class MessageParsingException extends Exception{
    public MessageParsingException(String message) {
        super(message);
    }

    public MessageParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
