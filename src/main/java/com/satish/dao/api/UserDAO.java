package com.satish.dao.api;

import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.filter.Filter;

import java.util.List;

/**
 * Data Access Layer interface for Users
 *
 * @author satish.thulva.
 **/
public interface UserDAO {

    long addUser(String firstName, String email, String passwordMd5, Role role, long creator);

    User findUser(long userId);

    User findUser(String email);

    String findUserPassword(String email);

    List<User> findUsers(long fromUserId, int numEntries, Filter filter);

    void updatePassword(long userId, String passwordMd5);

    void updateUser(long userId, String name, String email, boolean active);

    void updateActiveFlag(long actingUserId, long userIdToBeUpdated, boolean isActive);

    void updateRole(long actingUserId, long userIdToBeUpdated, Role role);

    long findUserCount();
}
