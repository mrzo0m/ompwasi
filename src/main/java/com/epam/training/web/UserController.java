package com.epam.training.web;

import com.epam.training.persistence.pojo.User;
import com.epam.training.persistence.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Oleg_Burshinov on 20.01.14.
 */
@Component
@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public long create(@RequestBody User user){
        return userService.create(user);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public @ResponseBody User read(@PathVariable String login){
        User user = new User();
        user.setLogin(login);
        return userService.read(user);
    }



}
