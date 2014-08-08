package com.epam.training.persistence.dao.mapper;

import com.epam.training.persistence.pojo.Bid;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
public class BidMapper implements ParameterizedRowMapper<Bid> {
    @Override
    public Bid mapRow(ResultSet resultSet, int i) throws SQLException {
        Bid bid = new Bid();
        bid.setBidderId(resultSet.getInt("BID_ID"));
        bid.setBidderId(resultSet.getInt("BIDDER_ID"));
        bid.setItemId(resultSet.getInt("ITEM_ID"));
        bid.setBid(resultSet.getDouble("BID"));
        return bid;
    }
}
