package com.fastcode.connector.service.soap;

import com.fastcode.connector.commons.exception.ConnectorException;
import com.fastcode.connector.commons.logging.ConnectorLoggingHelper;
import com.fastcode.connector.service.Connector;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SoapApiConnector implements Connector {

    @Autowired
    private ConnectorLoggingHelper logger;

    public SoapApiConnector(Map<String, Object> configurations) {
    }

    @Override
    public void start() {
        // Perform any necessary initialization tasks
    }

    @Override
    public void stop() {
        // Perform any necessary cleanup tasks
    }

    @Override
    public Object execute(Map<String, Object> parameters) throws ConnectorException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                //call soap service
                return null;
            } catch (Exception e) {
                logger.getLogger().error("Error executing Soap API call", e);
                throw new ConnectorException("Error executing Soap API call", e);
            }
        });
    }

    @Override
    public boolean testConnection() throws ConnectorException {
        try {
            //add test service url soap
            return true;
        } catch (Exception e) {
            logger.getLogger().error("Error testing Soap API connection", e);
            throw new ConnectorException("Error testing Soap API connection", e);
        }
    }
}
