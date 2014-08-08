package com.epam.training.persistence.dao;

import com.epam.training.persistence.dao.mapper.UserMapper;
import com.epam.training.persistence.dao.utils.Md5;
import com.epam.training.persistence.pojo.Bid;
import com.epam.training.persistence.pojo.BlackList;
import com.epam.training.persistence.pojo.Item;
import com.epam.training.persistence.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg_Burshinov on 17.01.14.
 */
@Repository
@Transactional
public class JdbcUserDao implements UserDao {

    private static final String QUERY_USER_INSERT = "INSERT INTO USERS (FULL_NAME,BILLING_ADDRESS,LOGIN,PASSWORD,EMAIL) VALUES (:fullName,:billingAddress,:login, :password, :email)";
    private static final String QUERY_GET_USER_ID = "SELECT USR_ID_GEN.nextval FROM dual";
    private static final String QUERY_IS_AUTHORIZED = "SELECT * FROM USERS WHERE LOGIN LIKE :login AND PASSWORD LIKE :password";
    private static final String QUERY_USER_BY_LOGIN = "SELECT * FROM USERS WHERE LOGIN LIKE :login";

    @Autowired
    private Md5 md5;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public long create(User user) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        int id = namedParameterJdbcTemplate.queryForInt(QUERY_GET_USER_ID, namedParameters);
        user.setUserId(id);
        user.setPassword(md5.getHash(user.getPassword()));
        namedParameters.put("fullName", user.getFullName());
        namedParameters.put("billingAddress", user.getBillingAddress());
        namedParameters.put("login", user.getLogin());
        namedParameters.put("password", user.getPassword());
        namedParameters.put("email", user.getEmail());
        namedParameterJdbcTemplate.update(QUERY_USER_INSERT, namedParameters);
        return id;
    }

    @Override
    public User read(User user) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("login", user.getLogin());
        List<User> users = namedParameterJdbcTemplate.query(QUERY_USER_BY_LOGIN, namedParameters, new UserMapper());
        User result = null;
        if (!users.isEmpty()) {
            result = users.get(0);
            if (result != null) {
                result.setPassword("");
            } else {
                result = user;
            }
        } else {
            result = user;
        }
        return result;
    }

    @Override
    public boolean authorize(User user) {
        boolean authorized = false;
        user.setPassword(md5.getHash(user.getPassword()));
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("login", user.getLogin());
        namedParameters.put("password", user.getPassword());
        List<User> users = namedParameterJdbcTemplate.query(QUERY_IS_AUTHORIZED, namedParameters, new UserMapper());
        if (!users.isEmpty()) {
            authorized = true;
        }
        return authorized;
    }


    @Override
    public Item[] UserItems(User user) {
        return new Item[0];
    }

    @Override
    public Bid addBid(User user) {
        return null;
    }


    @Override
    public Integer getUserIdByLogin(String login) {
        return null;
    }

    @Override
    public User getUserInfoById(Integer userId) {
        return null;
    }

    @Override
    public boolean addUserToBlackList(BlackList user) {
        return false;
    }

    @Override
    public boolean removeUserFromBlackList(BlackList user) {
        return false;
    }
}
