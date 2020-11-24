package com.abhinotes.kafka.m2o.service.exception;

public class AccountInfoServiceException extends Exception {
    public AccountInfoServiceException(String message) {
        super(message);
    }

    public AccountInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
