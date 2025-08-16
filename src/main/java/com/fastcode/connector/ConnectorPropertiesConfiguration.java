package com.fastcode.connector;

import com.fastcode.connector.commons.logging.ConnectorLoggingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConnectorPropertiesConfiguration {

    @Autowired
    private ConnectorLoggingHelper logHelper;

    @Autowired
    private Environment env;

    private static final String REST_API_BASE_URL_ENV = "REST_API_BASE_URL";
    private static final String REST_API_BASE_URL_SYSPROP = "rest.api.baseUrl";

    private static final String REST_API_API_KEY_ENV = "REST_API_API_KEY";
    private static final String REST_API_API_KEY_SYSPROP = "rest.api.apiKey";

    /**
     * @return the base URL for the REST API
     */
    public String getRestApiBaseUrl() {
        return getConfigurationProperty(REST_API_BASE_URL_ENV, REST_API_BASE_URL_SYSPROP, "https://example.com");
    }

    /**
     * @return the API key for the REST API
     */
    public String getRestApiApiKey() {
        return getConfigurationProperty(REST_API_API_KEY_ENV, REST_API_API_KEY_SYSPROP, null);
    }



    /**
     * Looks for the given key in the following places (in order):
     *
     * 1) Environment variables
     * 2) System Properties
     *
     * @param envKey
     * @param sysPropKey
     * @param defaultValue
     * @return the configured property value or default value if not found
     */
    private String getConfigurationProperty(String envKey, String sysPropKey, String defaultValue) {
        String value = env.getProperty(sysPropKey);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(envKey);
        }
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }
        logHelper.getLogger().debug("Config Property: {}/{} = {}", envKey, sysPropKey, value);
        return value;
    }
}