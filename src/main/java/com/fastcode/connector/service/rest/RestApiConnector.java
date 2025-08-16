package com.fastcode.connector.service.rest;

import com.fastcode.connector.commons.exception.ConnectorException;
import com.fastcode.connector.commons.logging.ConnectorLoggingHelper;
import com.fastcode.connector.service.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RestApiConnector implements Connector {

    @Autowired
    private ConnectorLoggingHelper logger;

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public RestApiConnector(Map<String, Object> configurations) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = (String) configurations.get("baseUrl");
        this.apiKey = (String) configurations.get("apiKey");
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
        try {
            String method = (String) parameters.get("method");
            String url = (String) parameters.get("url");
            Object body = parameters.get("body");

            Map<String, String> queryParams = null;
            Map<String, String> pathVariables = null;
            Map<String, String> headersMap = null;

            if (parameters.containsKey("queryParams")) {
                try {
                    queryParams = (Map<String, String>) parameters.get("queryParams");
                } catch (ClassCastException e) {
                    throw new ConnectorException("Invalid query parameters format", e);
                }
            }

            if (parameters.containsKey("pathVariables")) {
                try {
                    pathVariables = (Map<String, String>) parameters.get("pathVariables");
                } catch (ClassCastException e) {
                    throw new ConnectorException("Invalid path variables format", e);
                }
            }

            if (parameters.containsKey("headers")) {
                try {
                    headersMap = (Map<String, String>) parameters.get("headers");
                } catch (ClassCastException e) {
                    throw new ConnectorException("Invalid headers format", e);
                }
            }

            // Build the URL with query parameters
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + url);
            if (queryParams != null) {
                queryParams.forEach(uriBuilder::queryParam);
            }
            URI completeUri;
            try {
                completeUri = (pathVariables != null) ? uriBuilder.buildAndExpand(pathVariables).toUri() : uriBuilder.build().toUri();
            } catch (Exception e) {
                throw new ConnectorException("Error building complete URL", e);
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            if (headersMap != null) {
                headersMap.forEach(headers::add);
            }

            if (apiKey != null) {
                headers.add("Authorization", apiKey);
            }

            // Create the HTTP entity with headers and body
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            // Execute the request
            ResponseEntity<String> response;
            switch (method.toUpperCase()) {
                case "GET":
                    response = restTemplate.exchange(completeUri, HttpMethod.GET, entity, String.class);
                    break;
                case "POST":
                    response = restTemplate.exchange(completeUri, HttpMethod.POST, entity, String.class);
                    break;
                case "PUT":
                    response = restTemplate.exchange(completeUri, HttpMethod.PUT, entity, String.class);
                    break;
                case "DELETE":
                    response = restTemplate.exchange(completeUri, HttpMethod.DELETE, entity, String.class);
                    break;
                default:
                    throw new ConnectorException("Unsupported HTTP method: " + method);
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ConnectorException("Error executing REST API call", e);
        }
    }

    @Override
    public boolean testConnection() throws ConnectorException {
        try {
            restTemplate.getForObject(baseUrl, String.class);
            return true;
        } catch (Exception e) {
            logger.getLogger().error("Error testing REST API connection", e);
            throw new ConnectorException("Error testing REST API connection", e);
        }
    }
}
