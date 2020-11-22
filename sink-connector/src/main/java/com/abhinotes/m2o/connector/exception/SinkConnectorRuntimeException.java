package com.abhinotes.m2o.connector.exception;

public class SinkConnectorRuntimeException extends RuntimeException {

    public SinkConnectorRuntimeException(String message) {
        super(message);
    }

    public SinkConnectorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
