package com.epam.training.persistence.dao;

import com.epam.training.persistence.pojo.Bid;
import com.epam.training.persistence.pojo.BlackList;
import com.epam.training.persistence.pojo.Item;
import com.epam.training.persistence.pojo.User;

/**
 * Created by Oleg_Burshinov on 16.01.14.
 */
public interface UserDao {

    /**
     * @param user
     * @return  description  not added yet
     */
    public long create(User user);

    /**
     * @param user
     * @return  description  not added yet
     */
    public boolean authorize(User user);


    /**
     * @param user
     * @return description  not added yet  
     */
    public Item[] UserItems(User user);

    /**
     * @param user
     * @return  description  not added yet
     */
    public Bid addBid(User user);

    /**
     * @param user with login
     * @return user info without password
     */
    public User read (User user);
    /**
     * @param login
     * @return user id
     */
    public Integer getUserIdByLogin(String login);

    /**
     * @param userId
     * @return information about user
     */
    public User getUserInfoById(Integer userId);

    /**
     * @param user
     * @return status
     */
    public boolean addUserToBlackList(BlackList user);

    /**
     * @param user
     * @return status
     */
    public boolean removeUserFromBlackList(BlackList user);
}
