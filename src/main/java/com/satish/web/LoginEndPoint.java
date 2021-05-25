package com.satish.web;

import com.satish.AuthRequired;
import com.satish.Constants;
import com.satish.accesscontrol.Actor;
import com.satish.accesscontrol.ActorImpl;
import com.satish.datamodels.User;
import com.satish.dto.UserCreds;
import com.satish.service.api.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * @author satish.thulva
 **/
@Path("web")
public class LoginEndPoint {
    private final UserService userService;
    private final CookieVerifier cookieVerifier;

    @Inject
    public LoginEndPoint(UserService userService, CookieVerifier cookieVerifier) {
        this.userService = userService;
        this.cookieVerifier = cookieVerifier;
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCreds creds) {
        Pair<Boolean, User> authResult = userService.authenticate(creds.getEmail(), creds.getPassword());
        if (authResult.getLeft() && authResult.getRight().isActive()) {
            NewCookie cookie = new NewCookie(Constants.AUTH_COOKIE_NAME, RandomStringUtils.randomAlphanumeric(30), "/", "", null,
                    3600, false);
            User user = authResult.getRight();
            Actor actor = new ActorImpl(user.getId().getId(), user.getRole());
            cookieVerifier.addCookie(actor, cookie.getValue());
            return Response.ok().cookie(cookie).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Path("logout")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response logout(@Context ContainerRequestContext requestContext) {
        Actor actor = (Actor) requestContext.getProperty(Constants.ACTOR_PROPERTY_IN_REQUEST_CONTEXT);
        cookieVerifier.expireCookie(actor.getId());
        return Response.ok().build();
    }
}
