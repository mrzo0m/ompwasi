package com.epam.training.persistence.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author Oleg_Burshinov Session scope object for user information and
 *         authorization status
 */
@Component("userContext")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContext {

    @Autowired
    private User user;

    private boolean authorized = false;

    public UserContext() {
        super();
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the authorized
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * @param authorized the authorized to set
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
