package com.abhinotes.m2o.connector.source.exception;

public class SourceConnectorRuntimeException extends RuntimeException {

    public SourceConnectorRuntimeException(String message) {
        super(message);
    }

    public SourceConnectorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
