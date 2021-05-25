package com.satish.datamodels;

/**
 * @author satish.thulva.
 **/
public enum Role {
    USER(0),
    MANAGER(1),
    ADMIN(2);

    private int priority;

    private Role(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
