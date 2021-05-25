package com.satish.dao.impl.jdbi.mappers;

import com.satish.datamodels.Role;
import com.satish.datamodels.User;
import com.satish.datamodels.UserID;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author satish.thulva.
 **/
public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        long userId = rs.getLong("user_id");
        String firstName = rs.getString("first_name");
        String email = rs.getString("email");
        Role role = Role.valueOf(rs.getString("app_role").toUpperCase());
        boolean active = rs.getBoolean("is_active");
        return new User(new UserID(userId), firstName, email, role, active);
    }
}
