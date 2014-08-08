package com.epam.training.persistence.dao;

import com.epam.training.persistence.pojo.Bid;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
public interface BidDao {
    /**
     * @param to Transfer object for bids
     * @return adding status: true if bid set or false
     */
    public boolean addBid(Bid to);
}
