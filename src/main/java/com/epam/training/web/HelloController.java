package com.epam.training.web;

import com.epam.training.persistence.pojo.Signin;
import com.epam.training.persistence.pojo.User;
import com.epam.training.persistence.pojo.UserContext;
import com.epam.training.persistence.service.ItemService;
import com.epam.training.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class HelloController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserContext userContext;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView root(ModelAndView mav) {
        Signin signin = new Signin();
        mav.getModelMap().put("signin", signin);
        mav.setViewName("Login");
        return mav;
    }

    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mav) {
        Signin signin = new Signin();
        mav.getModelMap().put("signin", signin);
        mav.setViewName("Login");
        return mav;
    }

    @RequestMapping(value = "/registration.htm", method = RequestMethod.GET)
    public ModelAndView registration(ModelAndView mav) {
        User user = new User();
        mav.getModelMap().put("user", user);
        mav.setViewName("Registration");
        return mav;
    }

    @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    public ModelAndView loginAfterReg(@Valid User user, BindingResult result, ModelAndView mav) {
        if (result.hasErrors()) {
            ModelAndView mavReg = new ModelAndView("redirect:/registration.htm");
            mav.addObject("redirectUrl", "/registration.htm");
            mavReg.getModelMap().put("user", user);
            mavReg.setViewName("Registration");
            return mavReg;
        }
        userService.create(user);
        Signin signin = new Signin();
        mav.getModelMap().put("signin", signin);
        mav.setViewName("Login");
        return mav;
    }


    @RequestMapping(value = "/currentuser", method = RequestMethod.GET)
    public
    @ResponseBody
    User currentUser() {
        if (userContext.isAuthorized()) {
            return userContext.getUser();
        }
        return new User();
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response) {
        userContext.setAuthorized(false);
        userContext.setUser(new User());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setMaxAge(0);
                cookies[i].setValue("");
            }
        }

        ModelAndView mav = new ModelAndView("redirect:/login.htm");
        mav.addObject("signin", new Signin());
        return mav;
    }

    @RequestMapping(value = "/showitems.htm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showItems(ModelAndView mav) {
        mav.addObject("userContext", userContext);
        mav.setViewName("ShowItems");
        return mav;
    }

    @RequestMapping(value = "/showitems.htm", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView submitForm(@Valid Signin signin, BindingResult result, ModelAndView mav) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/login.htm");
        }
        User user = new User();
        user.setLogin(signin.getLogin());
        user.setPassword(signin.getPassword());
        if (userService.authorizeUser(user)) {
            userContext.setAuthorized(true);
            userContext.setUser(userService.read(user));
        } else {
            return new ModelAndView("redirect:/login.htm");
        }
        mav.addObject("userContext", userContext);
        mav.setViewName("ShowItems");
        return mav;
    }


    @RequestMapping(value = "/showmyitems", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showmyitems(ModelAndView mav) {
        mav.addObject("userContext", userContext);
        mav.setViewName("ShowMyItems");
        return mav;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception/catchedException");
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }

}