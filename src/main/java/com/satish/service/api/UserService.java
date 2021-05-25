package com.satish.service.api;

import com.satish.accesscontrol.Actor;
import com.satish.accesscontrol.Requires;
import com.satish.accesscontrol.SelfAccess;
import com.satish.datamodels.PaginatedResult;
import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.UserID;
import com.satish.datamodels.filter.Filter;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author satish.thulva.
 **/
public interface UserService {

    /**
     * Access control free call for a new user to register himself/herself
     *
     * @param userName  Name of the user
     * @param userEmail Email of the user
     * @param password  Password chosen
     * @return UniqueId of the created user if successful
     */
    long registerSelf(String userName, String userEmail, String password);

    /**
     * Call allowed for a Manager or Admin to register a new user with given details. ActorId is logged as creator id
     * for audit
     *
     * @param actor     UniqueId of the user who is creating the new user
     * @param userName  Name of the new user
     * @param userEmail Email of the new user
     * @param password  Password of the new user
     * @param role      Role of the new user
     * @return UniqueId of the created user if successful
     */
    @Requires(Role.MANAGER)
    long registerUser(Actor actor, String userName, String userEmail, String password,
                      Role role);

    /**
     * Change role of user. This is allowed for Admin only
     *
     * @param actor         UniqueId of the user who is changing the role
     * @param updatedUserId UniqueId of the user whose role is changing
     * @param role          New role for the user
     */
    @Requires(Role.ADMIN)
    void changeRole(Actor actor, UserID updatedUserId, Role role);

    /**
     * Update password of the user. Either Manager can do this for a user or user can do for his/her own account
     * NOTE : we do not store direct passwords in the system. MD5 of the user provided password is stored
     * and comparison while logging in happens w.r.t. that.
     *
     * @param actor    UniqueId of the acting user
     * @param userId   UniqueId of the user to update password for
     * @param password New password
     */
    @Requires(Role.MANAGER)
    @SelfAccess
    void updatePassword(Actor actor, UserID userId, String password);

    @Requires(Role.MANAGER)
    @SelfAccess
    void updateUser(Actor actor, UserID userID, String firstName, String email, boolean active);

    /**
     * Reset the password of user. Either Manager can do this for a user or user can do for his/her own account
     * TODO : Mechanism to be built for offline password reset. We can email new password to the user
     *
     * @param actor  UniqueId of the acting user
     * @param userId UniqueId of the user being acted upon
     */
    @Requires(Role.MANAGER)
    @SelfAccess
    void resetPassword(Actor actor, UserID userId);

    /**
     * Deactivate a user profile. This does not delete the data from the system. Account can be reactivated again.
     * Either Manager can do this for a user or user can do for his/her own account.
     *
     * @param actor  UniqueId of the acting user
     * @param userId UniqueId of the user being acted upon
     */
    @Requires(Role.MANAGER)
    @SelfAccess
    void deactivateUser(Actor actor, UserID userId);

    /**
     * Activate account of given user.
     * Either Manager can do this for a user or user can do for his/her own account
     *
     * @param actor  UniqueId of the acting user
     * @param userId UniqueId of the user being acted upon
     */
    @Requires(Role.MANAGER)
    void activateUser(Actor actor, UserID userId);

    /**
     * Authenticate user. Verify whether given password is correct or not.
     * This is access control free flow as user is not logged in yet.
     *
     * @param email    User email
     * @param password Password
     * @return <code>true</code>, if authentication is successful. <code>false</code>, otherwise
     */
    Pair<Boolean, User> authenticate(String email, String password);

    /**
     * List the next page of user listing (in the order of their account creation). Only Manager and above can do this.
     *
     * @param actor      UniqueId of the user making the call
     * @param fromUserId Number of users already scrolled over till now
     * @param limit      Number of users to fetch in the page
     * @return Next page in user listing
     */
    @Requires(Role.MANAGER)
    PaginatedResult<User> listUsers(Actor actor, UserID fromUserId, int limit, Filter filter);

    /**
     * Find user with given id.
     * Either Manager can do this for a user or user can do for his/her own account (for user details page ?).
     *
     * @param actor  UniqueId of the user making the call
     * @param userId UniqueId of the user to be fetched
     * @return User details, if found
     */
    @Requires(Role.MANAGER)
    @SelfAccess
    User findUser(Actor actor, UserID userId);

}
