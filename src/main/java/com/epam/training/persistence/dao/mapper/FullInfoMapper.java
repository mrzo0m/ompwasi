package com.epam.training.persistence.dao.mapper;

import com.epam.training.persistence.dao.ItemOrders;
import com.epam.training.persistence.pojo.FullInfo;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
public class FullInfoMapper implements ParameterizedRowMapper<FullInfo> {
    @Override
    public FullInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        FullInfo fullInfo = new FullInfo();
        fullInfo.setUid(resultSet.getInt("UID"));
        fullInfo.setTitle(resultSet.getString("TITLE"));
        fullInfo.setDescription(resultSet.getString("DESCRIPTION"));
        fullInfo.setSeller(resultSet.getString("SELLER"));
        fullInfo.setStartPrice(resultSet.getDouble("START_PRICE"));
        fullInfo.setBidIncrement(resultSet.getInt("BID_INCREMENT"));
        fullInfo.setBestOffer(resultSet.getDouble("BEST_OFFER"));
        fullInfo.setBidder(resultSet.getString("BIDDER"));
        Calendar stopDate = null;
        if(resultSet.getTimestamp("STOP_DATE") != null && !resultSet.wasNull()){
            Timestamp timestamp = resultSet.getTimestamp("STOP_DATE");
            stopDate = Calendar.getInstance();
            stopDate.setTimeInMillis(timestamp.getTime());
            fullInfo.setStopDate(stopDate);
        }
        boolean buyItNow = false;
        int tmp = resultSet.getInt("BUYITNOW");
        if (tmp == 1){
            buyItNow = true;
        }
        fullInfo.setBuyItNow(buyItNow);
        fullInfo.setCategory(resultSet.getString("CATEGORY"));
        return fullInfo;
    }
}
