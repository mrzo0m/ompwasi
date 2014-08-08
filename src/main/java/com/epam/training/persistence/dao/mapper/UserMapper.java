package com.epam.training.persistence.dao.mapper;

import com.epam.training.persistence.pojo.User;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Oleg_Burshinov on 17.01.14.
 */
public class UserMapper implements ParameterizedRowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("USER_ID"));
        user.setFullName(resultSet.getString("FULL_NAME"));
        user.setBillingAddress(resultSet.getString("BILLING_ADDRESS"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setPassword(resultSet.getString("PASSWORD"));
        user.setEmail(resultSet.getString("EMAIL"));

        return user;
    }
}
