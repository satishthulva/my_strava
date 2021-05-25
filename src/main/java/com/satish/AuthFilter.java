package com.satish;

import com.satish.accesscontrol.Actor;
import com.satish.rest.v1.TokenVerifier;
import com.satish.web.CookieVerifier;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static com.satish.Constants.*;

/**
 * Authenticates user and also sets the current Actor info to request
 * context to be used everywhere
 *
 * @author satish.thulva
 **/
@AuthRequired
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    private final CookieVerifier cookieVerifier;
    private final TokenVerifier tokenVerifier;

    @Inject
    public AuthFilter(CookieVerifier cookieVerifier, TokenVerifier tokenVerifier) {
        this.cookieVerifier = cookieVerifier;
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Check if cookie exists and valid
        if (isValidBasedOnCookie(requestContext)) {
            return;
        }

        // Check if token exists and valid
        if (isValidBasedOnToken(requestContext)) {
            return;
        }

        // If flow reached here --> auth failed
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTH_SCHEME + " realm=\"" + "RUNNER_LOG" + "\"")
                .build());
    }

    // Authentication based on cookie -- used for web browser
    private boolean isValidBasedOnCookie(ContainerRequestContext requestContext) {
        Map<String, Cookie> cookieMap = requestContext.getCookies();
        if (MapUtils.isEmpty(cookieMap)) {
            return false;
        }

        Cookie authCookie = cookieMap.get(AUTH_COOKIE_NAME);
        if (authCookie == null) {
            return false;
        }

        Actor actor = cookieVerifier.getUserIfValid(authCookie.getValue());
        if (actor != null) {
            setCurrentUser(requestContext, actor);
            return true;
        }

        return false;
    }

    // Authentication based on token -- used for REST API calls
    private boolean isValidBasedOnToken(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.toLowerCase().startsWith(AUTH_SCHEME.toLowerCase() + " ")) {
            return false;
        }

        String authToken = authHeader.substring(AUTH_SCHEME.length()).trim();
        Actor actor = tokenVerifier.getUserIfValid(authToken);
        if (actor != null) {
            setCurrentUser(requestContext, actor);
            return true;
        }

        return false;
    }

    private void setCurrentUser(ContainerRequestContext requestContext, Actor actor) {
        requestContext.setProperty(ACTOR_PROPERTY_IN_REQUEST_CONTEXT, actor);
    }

}
