package com.satish.accesscontrol;

/**
 * Custom exception thrown when Access is not allowed
 *
 * @author satish.thulva
 **/
public class AccessControlException extends RuntimeException {
    public AccessControlException(String message) {
        super(message);
    }
}
