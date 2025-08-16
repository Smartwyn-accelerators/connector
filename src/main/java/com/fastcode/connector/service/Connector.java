package com.fastcode.connector.service;

import com.fastcode.connector.commons.exception.ConnectorException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Connector {
    void start();
    void stop();
    Object execute(Map<String, Object> parameters) throws ConnectorException;
    boolean testConnection() throws ConnectorException;
}

