package com.epam.training.persistence.dao;

import com.epam.training.persistence.dao.mapper.BlackListMapper;
import com.epam.training.persistence.dao.mapper.ItemMapper;
import com.epam.training.persistence.pojo.Bid;
import com.epam.training.persistence.pojo.BlackList;
import com.epam.training.persistence.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
@Repository
@Transactional
public class JdbcBidDao implements BidDao {

    private final static double EPSILON = 0.00001;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String QUERY_ITEM_INFO = "SELECT * FROM ITEMS WHERE item_id = :itemId";
    private static final String QUERY_IS_IN_BLACK_LIST = "SELECT * FROM BLACKLIST WHERE user_id = :userId AND black_user_id = :blackUserId";
    private static final String QUERY_BESTOFFER = "SELECT MAX(BID)AS BESTOFFER FROM BIDS WHERE item_id = :itemId";
    private static final String QUERY_BID = "INSERT INTO BIDS (BIDDER_ID, ITEM_ID, BID) VALUES (:bidderId, :itemId, :bid)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public boolean addBid(Bid to) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        int inserted = 0;
        double bidInc = 0;
        double startPrice = 0;
        double bestOffer = 0;
        int ownerId = 0;
        namedParameters.put("itemId", to.getItemId());
        List<Item> items = namedParameterJdbcTemplate.query(QUERY_ITEM_INFO, namedParameters, new ItemMapper());
        Item item = null;
        if (items.isEmpty()) {
            return false;
        }
        item = items.get(0);
        bidInc = item.getBidIncrement();
        ownerId = item.getSellerId();
        startPrice = item.getStartPrice();
        namedParameters.clear();
        namedParameters.put("userId", ownerId);
        namedParameters.put("blackUserId", to.getBidderId());
        List<BlackList> blackList = namedParameterJdbcTemplate.query(QUERY_IS_IN_BLACK_LIST, namedParameters, new BlackListMapper());
        if (!blackList.isEmpty()) {
            return false;
        }
        namedParameters.clear();
        if(item.isBuyItNow()){
            namedParameters.put("bidderId", to.getBidderId());
            namedParameters.put("itemId", to.getItemId());
            namedParameters.put("bid", to.getStartPrice());
            inserted = namedParameterJdbcTemplate.update(QUERY_BID, namedParameters);
        }
        namedParameters.clear();
        namedParameters.put("itemId", to.getItemId());
        List<Double> bidBestOffer = namedParameterJdbcTemplate.query(QUERY_BESTOFFER, namedParameters, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble("BESTOFFER");
            }
        });
        namedParameters.clear();
        if (!bidBestOffer.isEmpty()) {
            bestOffer = bidBestOffer.get(0);
        }
        double goodbid = startPrice + bidInc;
        if (bestOffer > 0) {
            goodbid = bestOffer + bidInc;
        }
        if (bidInc == 0 && bestOffer > 0) {
            return false;
        }

        if ((Math.abs(goodbid - to.getBid()) < EPSILON) || (to.getBid() > goodbid)) {
            namedParameters.put("bidderId", to.getBidderId());
            namedParameters.put("itemId", to.getItemId());
            namedParameters.put("bid", to.getBid());
            inserted = namedParameterJdbcTemplate.update(QUERY_BID, namedParameters);
        }
        if (inserted > 0) {
            return true;
        }
        return false;
    }
}
