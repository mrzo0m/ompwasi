package com.epam.training.persistence.service;

import com.epam.training.persistence.dao.BidDao;
import com.epam.training.persistence.pojo.Bid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
@Service
public class BidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidService.class);

    @Autowired
    private BidDao bidDao;

    public BidService() {
        super();
    }

    public boolean addBid(Bid to){
        return  bidDao.addBid(to);
    }
}
