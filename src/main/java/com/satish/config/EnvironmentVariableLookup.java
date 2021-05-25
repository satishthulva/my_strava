package com.satish.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.lookup.StringLookup;

/**
 * Utility to look up OS env or JVM arguments for variables
 *
 * @author satish.thulva
 */
public class EnvironmentVariableLookup implements StringLookup {

    @Override
    public String lookup(String key) {
        // Priority to OS level environmental variable first
        String value = System.getenv(key);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        } else {
            // Environmental variable not set --> check for JVM argument
            value = System.getProperty(key);
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }
}
