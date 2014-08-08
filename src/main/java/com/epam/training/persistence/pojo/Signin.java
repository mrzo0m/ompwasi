package com.epam.training.persistence.pojo;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Oleg_Burshinov on 22.01.14.
 */
@Component
public class Signin {
    @NotNull
    @Size(min=2, max=30)
    private String login;
    @NotNull(message = "${password.min.length}")
    @Size(min=6, max=30)
    private String password;

    public Signin() {
        super();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signin signin = (Signin) o;

        if (login != null ? !login.equals(signin.login) : signin.login != null) return false;
        if (password != null ? !password.equals(signin.password) : signin.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Signin{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
