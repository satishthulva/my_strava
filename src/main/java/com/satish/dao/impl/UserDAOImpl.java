package com.satish.dao.impl;

import com.satish.dao.api.UserDAO;
import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.filter.Filter;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.inject.Inject;
import java.util.List;

/**
 * @author satish.thulva.
 **/
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    @Inject
    public UserDAOImpl(DBDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public long addUser(String firstName, String email, String passwordMd5, Role role, long creator) {
        return jdbi.withExtension(UserDAOJdbi.class,
                d -> d.insertUser(firstName, email, passwordMd5, role.name(), creator));
    }

    @Override
    public User findUser(long userId) {
        return jdbi.withExtension(UserDAOJdbi.class, d -> d.findUserById(userId));
    }

    @Override
    public User findUser(String email) {
        return jdbi.withExtension(UserDAOJdbi.class, d -> d.findUserByEmail(email));
    }

    @Override
    public String findUserPassword(String email) {
        return jdbi.withExtension(UserDAOJdbi.class, d -> d.findUserPassword(email));
    }

    @Override
    public List<User> findUsers(long fromUserId, int numEntries, Filter filter) {
        return jdbi.withExtension(UserDAOJdbi.class,
                d -> filter != null
                     ? d.listUsersWithFilter(fromUserId, filter.toQueryComponent(), numEntries)
                     : d.listUsersNoFilter(fromUserId, numEntries));
    }

    @Override
    public void updatePassword(long userId, String passwordMd5) {
        jdbi.useExtension(UserDAOJdbi.class, e -> e.updatePassword(userId, passwordMd5));
    }

    @Override
    public void updateUser(long userId, String name, String email, boolean active) {
        jdbi.useExtension(UserDAOJdbi.class, e -> e.updateUser(userId, name, email, active));
    }

    @Override
    public void updateActiveFlag(long actingUserId, long userIdToBeUpdated, boolean isActive) {
        jdbi.useExtension(UserDAOJdbi.class, d -> d.updateActiveFlag(userIdToBeUpdated, isActive, actingUserId));
    }

    @Override
    public void updateRole(long actingUserId, long userIdToBeUpdated, Role role) {
        jdbi.useExtension(UserDAOJdbi.class, d -> d.updateRole(userIdToBeUpdated, role.name(), actingUserId));
    }

    @Override
    public long findUserCount() {
        return jdbi.withExtension(UserDAOJdbi.class, UserDAOJdbi::findUserCount);
    }

    private interface UserDAOJdbi {
        @SqlUpdate("INSERT INTO users(first_name, email, password_md5, app_role, created_by)" +
                           " VALUES (:firstName, :email, :passwordMd5, :appRole, :createdBy)")
        @GetGeneratedKeys
        long insertUser(@Bind("firstName") String firstName, @Bind("email") String email,
                        @Bind("passwordMd5") String passwordMd5, @Bind("appRole") String appRole,
                        @Bind("createdBy") long createdBy);

        @SqlQuery("SELECT * FROM users WHERE user_id = :userId")
        User findUserById(@Bind("userId") long userId);

        @SqlQuery("SELECT * FROM users WHERE email = :email")
        User findUserByEmail(@Bind("email") String email);

        @SqlQuery("SELECT password_md5 FROM users WHERE email = :email")
        String findUserPassword(@Bind("email") String email);

        @SqlQuery("SELECT * FROM users WHERE user_id > :userId AND :filterCondition ORDER BY user_id LIMIT :numEntries")
        List<User> listUsersWithFilter(@Bind("userId") long userId, @Bind("filterCondition") String filterCondition,
                                       @Bind("numEntries") int numEntries);

        @SqlQuery("SELECT * FROM users WHERE user_id > :userId ORDER BY user_id LIMIT :numEntries")
        List<User> listUsersNoFilter(@Bind("userId") long userId, @Bind("numEntries") int numEntries);

        @SqlUpdate("UPDATE users SET password_md5 = :passwordMd5 WHERE user_id = :userId")
        void updatePassword(@Bind("userId") long id, @Bind("passwordMd5") String passwordMd5);

        @SqlUpdate("UPDATE users SET first_name = :name, email = :email, is_active = :isActive WHERE user_id = :userId")
        void updateUser(@Bind("userId") long id, @Bind("name") String name, @Bind("email") String email,
                        @Bind("isActive") boolean isActive);

        @SqlUpdate("UPDATE users SET is_active = :isActive WHERE user_id = :userId")
        void updateActiveFlag(@Bind("userId") long id, @Bind("isActive") boolean isActive,
                              @Bind("updatedBy") long updatedBy);

        @SqlUpdate("UPDATE users SET app_role = :appRole WHERE user_id = :userId")
        void updateRole(@Bind("userId") long id, @Bind("appRole") String appRole, @Bind("updatedBy") long updatedBy);

        @SqlQuery("SELECT COUNT(*) FROM users")
        long findUserCount();
    }
}
