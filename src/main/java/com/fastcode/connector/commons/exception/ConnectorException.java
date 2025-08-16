package com.fastcode.connector.commons.exception;

public class ConnectorException extends RuntimeException{
    public ConnectorException(String message) {
        super(message);
    }

    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
