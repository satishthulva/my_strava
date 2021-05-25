package com.satish.accesscontrol;

import com.satish.datamodels.Role;

/**
 * Identifier for the logged in user
 *
 * @author satish.thulva
 **/
public interface Actor {
    long getId();

    Role getRole();
}
