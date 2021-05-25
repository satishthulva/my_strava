package com.satish.rest.v1;

import com.satish.AuthRequired;
import com.satish.accesscontrol.Actor;
import com.satish.accesscontrol.ActorImpl;
import com.satish.datamodels.PaginatedResult;
import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.UserID;
import com.satish.dto.CreateUser;
import com.satish.dto.UpdateUser;
import com.satish.dto.UserCreds;
import com.satish.service.api.UserService;
import com.satish.Constants;
import com.satish.utils.FilterParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST API for users resource
 *
 * @author satish.thulva
 **/
@Path("rest/v1/users")
public class Users extends AbstractRestEndPoint {

    private final UserService userService;
    private final TokenVerifier tokenVerifier;

    @Inject
    public Users(UserService userService, TokenVerifier tokenVerifier) {
        this.userService = userService;
        this.tokenVerifier = tokenVerifier;
    }

    /**
     * Free call for a user to register him/herself
     */
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public long selfRegister(CreateUser createUser) {
        return userService.registerSelf(createUser.getName(), createUser.getEmail(), createUser.getPassword());
    }

    /**
     * Call accessible to Manager and Admin only to invite new users
     */
    @Path("invite")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public long inviteUser(CreateUser createUser) {
        Actor actor = getActor();
        return userService.registerUser(actor, createUser.getName(), createUser.getEmail(), createUser.getPassword(),
                Role.valueOf(createUser.getRole().toUpperCase()));
    }

    /**
     * Call available to generate access token
     */
    @Path("generate_token")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateToken(UserCreds creds) {
        Pair<Boolean, User> authResult = userService.authenticate(creds.getEmail(), creds.getPassword());
        if (authResult.getLeft() && authResult.getRight().isActive()) {
            String token = RandomStringUtils.randomAlphanumeric(Constants.TOKEN_LENGTH);
            User user = authResult.getRight();
            Actor actor = new ActorImpl(user.getId().getId(), user.getRole());
            tokenVerifier.addToken(actor, token);
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * Call to revoke currently used access token
     */
    @Path("revoke_token")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public void revokeToken() {
        Actor actor = getActor();
        tokenVerifier.expireToken(actor.getId());
    }

    /**
     * User listing available to Manager and Admin only. Other users will get exception if tried
     *
     * @param offsetUserId UserId returned in previous page of the listing, if any
     * @param pageSize     Max number of entries to return
     * @param filter       Any filtering logic to apply. This is an encoded infix notation
     * @return Next page of user listing satisfying given filter
     */
    @Path("list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public PaginatedResult<User> listAll(@QueryParam("offset_user_id") @DefaultValue("1") long offsetUserId,
                                         @QueryParam("page_size") @DefaultValue("30") int pageSize,
                                         @QueryParam("filter") @DefaultValue("") String filter) {
        Actor actor = getActor();
        UserID fromUserId = new UserID(offsetUserId);
        return userService.listUsers(actor, fromUserId, pageSize, FilterParser.parse(filter));
    }

    /**
     * Fetch info of user with given Id. Open to the user him/herself, manager, and admin
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public User findById(@PathParam("id") long userId) {
        Actor actor = getActor();
        UserID userIdObject = new UserID(userId);
        return userService.findUser(actor, userIdObject);
    }

    @Path("{id}/change_role")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public void changeRole(@PathParam("id") long userId, @QueryParam("role") String role) {
        Actor actor = getActor();
        UserID updatedUserId = new UserID(userId);
        userService.changeRole(actor, updatedUserId, Role.valueOf(role.toUpperCase()));
    }


    @Path("{id}/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public void update(@PathParam("id") long userId, UpdateUser updateUser) {
        Actor actor = getActor();
        UserID updatedUserId = new UserID(userId);
        userService.updateUser(actor, updatedUserId, updateUser.getFirstName(), updateUser.getEmail(),
                updateUser.isActive());
    }

}
