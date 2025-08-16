package com.fastcode.connector;

import com.fastcode.connector.commons.Constants;
import com.fastcode.connector.service.ConnectorFactory;
import com.fastcode.connector.service.rest.RestApiConnector;
import com.fastcode.connector.service.soap.SoapApiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConnectorConfiguration {

    @Autowired
    private ConnectorFactory connectorFactory;

    @Autowired
    private ConnectorPropertiesConfiguration env;

    @PostConstruct
    public void registerConnectors() {
        Map<String, Object> restApiConnectorConfig = new HashMap<>();
        restApiConnectorConfig.put("baseUrl", env.getRestApiBaseUrl());
        restApiConnectorConfig.put("apiKey", env.getRestApiApiKey());
        connectorFactory.registerConnector(Constants.REST_API_CONNECTOR, new RestApiConnector(restApiConnectorConfig), restApiConnectorConfig);

        Map<String, Object> soapApiConnectorConfig = new HashMap<>();
        //add basic configuration
        connectorFactory.registerConnector(Constants.SOAP_API_CONNECTOR, new SoapApiConnector(soapApiConnectorConfig), soapApiConnectorConfig);

        // Register other connectors here if necessary
    }
}

