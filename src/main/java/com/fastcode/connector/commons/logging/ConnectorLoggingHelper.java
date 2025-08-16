package com.fastcode.connector.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectorLoggingHelper {

    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}

