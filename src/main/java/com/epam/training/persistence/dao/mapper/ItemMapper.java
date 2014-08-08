package com.epam.training.persistence.dao.mapper;

import com.epam.training.persistence.pojo.Item;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Oleg_Burshinov on 20.01.14.
 */
public class ItemMapper implements ParameterizedRowMapper<Item> {
    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        Item item = new Item();
        item.setItemId(resultSet.getInt("ITEM_ID"));
        item.setSellerId(resultSet.getInt("SELLER_ID"));
        item.setTitle(resultSet.getString("TITLE"));
        item.setDescription(resultSet.getString("DESCRIPTION"));
        item.setStartPrice(resultSet.getDouble("START_PRICE"));
        item.setTimeLeft(resultSet.getInt("TIME_LEFT"));
        Calendar startBiddingDate = Calendar.getInstance();
        startBiddingDate.setTime(resultSet.getTimestamp("START_BIDDING_DATE"));
        item.setStartBiddingDate(startBiddingDate);
        boolean buyItNow = false;
        int tmp = resultSet.getInt("BUY_IT_NOW");
        if (tmp == 1){
            buyItNow = true;
        }
        item.setBuyItNow(buyItNow);
        item.setBidIncrement(resultSet.getInt("BID_INCREMENT"));
        item.setCategory(resultSet.getInt("CATEGORY"));
        return item;
    }
}
