package com.epam.training.persistence.service;

import com.epam.training.persistence.dao.UserDao;
import com.epam.training.persistence.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Oleg_Burshinov on 16.01.14.
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    public UserService() {
        super();
    }

    public long create (final User user){
        LOGGER.debug("Creating a new user with information: " + user);
        return userDao.create(user);
    }

    public  User read (final  User user) {
        LOGGER.debug("Get information about: " + user.getLogin());
        return  userDao.read(user);
    }

    public  boolean authorizeUser (final  User user) {
        LOGGER.debug("authorize a user: " + user);
        boolean result = userDao.authorize(user);
        LOGGER.debug("authorize status: " + result);
        return result;
    }

}
