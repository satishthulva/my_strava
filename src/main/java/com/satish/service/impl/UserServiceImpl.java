package com.satish.service.impl;

import com.satish.accesscontrol.Actor;
import com.satish.datamodels.PaginatedResult;
import com.satish.datamodels.UserID;
import com.satish.dao.api.UserDAO;
import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.filter.Filter;
import com.satish.service.api.UserService;
import com.satish.utils.MD5Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * @author satish.thulva.
 **/
public class UserServiceImpl implements UserService {
    private static final int PASSWORD_SALT_LENGTH = 4;

    @Inject
    private UserDAO userDAO;

    @Override
    public long registerUser(Actor actor, String userName, String userEmail,
                             String password, Role role) {
        String passwordMd5 = generateSaltedMd5Password(password);
        return userDAO.addUser(userName, userEmail, passwordMd5, role, actor.getId());
    }

    @Override
    public long registerSelf(String userName, String userEmail, String password) {
        String passwordMd5 = generateSaltedMd5Password(password);
        return userDAO.addUser(userName, userEmail, passwordMd5, Role.USER, -1);
    }

    @Override
    public void changeRole(Actor actor, UserID updatedUserId, Role role) {
        userDAO.updateRole(actor.getId(), updatedUserId.getId(), role);
    }

    @Override
    public void updatePassword(Actor actor, UserID userId, String password) {
        String passwordMd5 = generateSaltedMd5Password(password);
        userDAO.updatePassword(userId.getId(), passwordMd5);
    }

    @Override
    public void updateUser(Actor actor, UserID userID, String firstName, String email, boolean active) {
        userDAO.updateUser(userID.getId(), firstName, email, active);
    }

    @Override
    public void resetPassword(Actor actor, UserID userId) {
        userDAO.updatePassword(userId.getId(), "");
    }

    @Override
    public void deactivateUser(Actor actor, UserID userId) {
        userDAO.updateActiveFlag(actor.getId(), userId.getId(), false);
    }

    @Override
    public void activateUser(Actor actor, UserID userId) {
        userDAO.updateActiveFlag(actor.getId(), userId.getId(), true);
    }

    @Override
    public Pair<Boolean, User> authenticate(String email, String password) {
        String encryptedPassword = userDAO.findUserPassword(email);
        // Can happen when password is not set
        if (encryptedPassword.length() < PASSWORD_SALT_LENGTH) {
            return Pair.of(false, null);
        }
        String salt = encryptedPassword.substring(0, PASSWORD_SALT_LENGTH);
        String saltAndPasswordMd5 = encryptedPassword.substring(PASSWORD_SALT_LENGTH);
        boolean isValidPwd = saltAndPasswordMd5.equals(MD5Utils.md5AsHexString(salt + password));
        if (isValidPwd) {
            User user = userDAO.findUser(email);
            return Pair.of(true, user);
        } else {
            return Pair.of(false, null);
        }
    }

    @Override
    public PaginatedResult<User> listUsers(Actor actor, UserID fromUserId, int limit, Filter filter) {
        long userCount = userDAO.findUserCount();
        if (limit > userCount) {
            limit = (int) userCount;
        }
        List<User> users = userDAO.findUsers(fromUserId.getId(), limit, filter);
        if (users == null) {
            users = Collections.emptyList();
        }
        return new PaginatedResult<>(users, userCount, users.size());
    }

    @Override
    public User findUser(Actor actor, UserID userId) {
        return userDAO.findUser(userId.getId());
    }

    /**
     * Mechanism to make sure passwords can't be stolen from the DB
     * Password is not directly stored. A random salt of length {@link UserServiceImpl#PASSWORD_SALT_LENGTH}
     * is prefixed with the password and MD5 is generated. To verify the password, as we need same salt to regenerate
     * MD5, salt itself is stored again as a prefix without any modification
     */
    private String generateSaltedMd5Password(String password) {
        String salt = RandomStringUtils.randomAlphanumeric(PASSWORD_SALT_LENGTH);
        // To make sure same password used again will have different representation in DB
        String saltPrefixWithPassword = salt + password;
        String md5 = MD5Utils.md5AsHexString(saltPrefixWithPassword);
        // To be able to verify the user claim during authentication
        return salt + md5;
    }

}
