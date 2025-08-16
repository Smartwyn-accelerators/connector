package com.fastcode.connector.service;

import com.fastcode.connector.service.rest.RestApiConnector;
import com.fastcode.connector.service.soap.SoapApiConnector;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.fastcode.connector.commons.Constants.REST_API_CONNECTOR;
import static com.fastcode.connector.commons.Constants.SOAP_API_CONNECTOR;

@Service
public class ConnectorFactory {
    private final Map<String, Connector> connectorCache = new ConcurrentHashMap<>();

    public Connector getConnector(String connectorId, Map<String, Object> configurations) {
        String cacheKey = connectorId + configurations.hashCode();
        if (!connectorCache.containsKey(cacheKey)) {
            Connector connector = createConnector(connectorId, configurations);
            connector.start();
            connectorCache.put(cacheKey, connector);
        }
        return connectorCache.get(cacheKey);
    }

    private Connector createConnector(String connectorId, Map<String, Object> configurations) {
        Connector connector;
        switch (connectorId) {
            case REST_API_CONNECTOR:
                connector = new RestApiConnector(configurations);
                break;
            case SOAP_API_CONNECTOR:
                connector = new SoapApiConnector(configurations);
                break;
            // Add more connector implementations here
            default:
                throw new IllegalArgumentException("Invalid connectorId");
        }
        return connector;
    }

    public void registerConnector(String connectorId, Connector connector, Map<String, Object> configurations) {
        String cacheKey = connectorId + configurations.hashCode();
        connectorCache.put(cacheKey, connector);
    }

}

