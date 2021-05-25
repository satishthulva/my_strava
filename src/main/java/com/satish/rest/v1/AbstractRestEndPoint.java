package com.satish.rest.v1;

import com.satish.accesscontrol.Actor;
import com.satish.Constants;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

/**
 * Common logic for all REST endpoints exposing the utility to
 * fetch current logged in user
 *
 * @author satish.thulva
 **/
public class AbstractRestEndPoint {

    @Context
    private ContainerRequestContext requestContext;

    protected Actor getActor() {
        return (Actor) requestContext.getProperty(Constants.ACTOR_PROPERTY_IN_REQUEST_CONTEXT);
    }
}
