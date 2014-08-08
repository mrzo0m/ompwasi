package com.epam.training.web;

import com.epam.training.persistence.pojo.Bid;
import com.epam.training.persistence.pojo.UserContext;
import com.epam.training.persistence.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
@Component
@Controller
@RequestMapping("/api/bid")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private UserContext userContext;

    @RequestMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public boolean addBid(@RequestBody Bid bid){
        bid.setBidderId(userContext.getUser().getUserId());
        return bidService.addBid(bid);
    }
}
