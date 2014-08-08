package com.epam.training.web;

import com.epam.training.persistence.pojo.AdvancedSearch;
import com.epam.training.persistence.pojo.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Oleg_Burshinov on 27.01.14.
 */
@Component
@Controller
public class SearchController {

    @Autowired
    private UserContext userContext;


    @RequestMapping(value = "clearsearch", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView clearsearch(HttpServletRequest request,
                                    HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (!cookies[i].getName().equals("JSESSIONID")) {
                    cookies[i].setMaxAge(0);
                    cookies[i].setValue("");
                    response.addCookie(cookies[i]);
                }
            }
        }
        return new ModelAndView("redirect:/advancedsearch.htm");

    }

    @RequestMapping(value = "/advancedsearch.htm", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView  submitForm(@Valid AdvancedSearch advancedSearch, BindingResult result, ModelAndView mav, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/showitems.htm");

        }
        if (advancedSearch.getItemUID() != null) {
            Cookie itemUID = new Cookie("itemUID", advancedSearch.getItemUID()
                    .toString());
            response.addCookie(itemUID);
        }
        if (advancedSearch.getTitleOfItem() != null) {
            Cookie titleOfItem = new Cookie("titleOfItem",
                    advancedSearch.getTitleOfItem());
            response.addCookie(titleOfItem);
        }
        if (advancedSearch.getDescription() != null) {
            Cookie description = new Cookie("description",
                    advancedSearch.getDescription());
            response.addCookie(description);
        }
        if (advancedSearch.getMinPrice() > 0) {
            Cookie minPrice = new Cookie("minPrice", Double.valueOf(
                    advancedSearch.getMinPrice()).toString());
            response.addCookie(minPrice);
        }
        if (advancedSearch.getMaxPrice() > 0) {
            Cookie maxPrice = new Cookie("maxPrice", Double.valueOf(
                    advancedSearch.getMaxPrice()).toString());
            response.addCookie(maxPrice);
        }
        if (advancedSearch.isBuyItNow()) {
            Cookie buyItNow = new Cookie("buyItNow", "true");
            response.addCookie(buyItNow);
        }
        if (advancedSearch.getStartDate() != null) {
            Cookie startDate = new Cookie("startDate", Long.toString(advancedSearch
                    .getStartDate().getTime()));
            response.addCookie(startDate);
        }
        if (advancedSearch.getExpireDate() != null) {
            Cookie expireDate = new Cookie("expireDate", Long.toString(advancedSearch
                    .getExpireDate().getTime()));
            response.addCookie(expireDate);
        }
        if (advancedSearch.getBiddersCount() != null) {
            Cookie biddersCount = new Cookie("biddersCount", advancedSearch
                    .getBiddersCount().toString());
            response.addCookie(biddersCount);
        }

        return new ModelAndView("redirect:/showitems.htm");
    }


    @RequestMapping(value = "/advancedsearch.htm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView advancedsearch(ModelAndView mav,
                                       @CookieValue(value = "itemUID", defaultValue = "0") String itemUID,
                                       @CookieValue(value = "titleOfItem", defaultValue = "") String titleOfItem,
                                       @CookieValue(value = "description", defaultValue = "") String description,
                                       @CookieValue(value = "minPrice", defaultValue = "0.0") String minPrice,
                                       @CookieValue(value = "maxPrice", defaultValue = "0.0") String maxPrice,
                                       @CookieValue(value = "buyItNow", defaultValue = "false") String buyItNow,
                                       @CookieValue(value = "startDate", defaultValue = "0") String startDate,
                                       @CookieValue(value = "expireDate", defaultValue = "0") String expireDate,
                                       @CookieValue(value = "biddersCount", defaultValue = "0") String biddersCount,
                                       HttpServletRequest request, HttpServletResponse response) {

        AdvancedSearch advancedSearch = new AdvancedSearch();
        advancedSearch.setItemUID(Integer.parseInt(itemUID));
        advancedSearch.setTitleOfItem(titleOfItem);
        advancedSearch.setDescription(description);
        advancedSearch.setMinPrice(Double.parseDouble(minPrice));
        advancedSearch.setMaxPrice(Double.parseDouble(maxPrice));
        if (buyItNow.equals("buyItNow")) {
            advancedSearch.setBuyItNow(true);
        }
        advancedSearch.setStartDate(new Date(Long.parseLong(startDate)));
        advancedSearch.setExpireDate(new Date(Long.parseLong(expireDate)));
        advancedSearch.setBiddersCount(Integer.parseInt(biddersCount));


        mav.addObject("userContext", userContext);
        mav.addObject("advancedSearch", advancedSearch);
        mav.setViewName("AdvancedSearchForm");
        return mav;
    }
}
