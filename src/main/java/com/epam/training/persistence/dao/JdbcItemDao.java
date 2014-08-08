package com.epam.training.persistence.dao;

import com.epam.training.persistence.dao.mapper.FullInfoMapper;
import com.epam.training.persistence.dao.mapper.ItemMapper;
import com.epam.training.persistence.dao.utils.Paging;
import com.epam.training.persistence.pojo.Category;
import com.epam.training.persistence.pojo.FullInfo;
import com.epam.training.persistence.pojo.History;
import com.epam.training.persistence.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Oleg_Burshinov on 20.01.14.
 */
@Repository
@Transactional
public class JdbcItemDao implements ItemDao {

    private static final String QUERY_GET_ITEM_SEQUENCE_CURRENT_VALUE = "SELECT UID_GEN.currval FROM dual";
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM ITEMS WHERE ITEM_ID = :id";
    private static final String QUERY_SOLDITEMS = "SELECT * FROM SOLDITEMS";
    private static final String QUERY_UPDATE = "UPDATE ITEMS SET TITLE = :title, DESCRIPTION = :description, START_PRICE = :startPrice, TIME_LEFT = :timeLeft, BUY_IT_NOW = :buyItNow, BID_INCREMENT = :bidIncrement WHERE item_id = :itemId";
    private static final String QUERY_INSERT = "INSERT into ITEMS (SELLER_ID,TITLE,DESCRIPTION,START_PRICE,TIME_LEFT,START_BIDDING_DATE,BUY_IT_NOW,BID_INCREMENT) values (:sellerId,:title,:description,:startPrice,:timeLeft,:startBiddingDate,:buyItNow,:bidIncrement)";
    private static final String QUERY_DELETE = "DELETE FROM ITEMS WHERE item_id = :itemId";

    private static final int CATEGORY_ROOT = 0;
    private static final String OREDER_DIRECTION = " ORDER BY %s %s NULLS LAST";
    private static final String PRICE_FILTER = " %s %s :price ";
//    private static final String DATE_VALUE =  "  %s  >= to_timestamp(:dateValue, 'dd-mm-yyyy hh24:mi:ss') ";
    private static final String DATE_VALUE = "(to_char(%s, 'YYYY-MM-DD HH24:MI') = to_char(:dateValue, 'YYYY-MM-DD HH24:MI'))";


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Set<Item> allItems(int pageNumber, int itemsCountOnPage) {
        return null;
    }

    @Override
    public Item findItemById(Integer id) {
        Item item = null;
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("id", id);
        List<Item> items = namedParameterJdbcTemplate.query(QUERY_FIND_BY_ID, namedParameters, new ItemMapper());
        if (!items.isEmpty()) {
            item = items.get(0);
        }
        return item;
    }

    @Override
    public Set<Item> findItemsByTitle(String title) {
        return null;
    }

    @Override
    public Set<Item> findItemsByDescription(String description) {
        return null;
    }

    @Override
    public int insertItem(Item to) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        namedParameters.put("sellerId", to.getSellerId());
        namedParameters.put("title",  to.getTitle());
        namedParameters.put("description", to.getDescription());
        namedParameters.put("startPrice", to.getStartPrice());
        namedParameters.put("timeLeft", to.getTimeLeft());
        namedParameters.put("startBiddingDate", null);
        if (to.isBuyItNow()) {
            namedParameters.put("buyItNow",  1);
        } else {
            namedParameters.put("buyItNow",  0);
        }
        namedParameters.put("bidIncrement", to.getBidIncrement());
        int inserted = namedParameterJdbcTemplate.update(QUERY_INSERT, namedParameters);

        int id = namedParameterJdbcTemplate.queryForInt(QUERY_GET_ITEM_SEQUENCE_CURRENT_VALUE, namedParameters);
        if (inserted > 0) {
            return id;
        }
        return -1;
    }

    @Override
    public boolean deleteItem(Integer id) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        boolean result = false;
        namedParameters.put("itemId", id);
        int inserted = namedParameterJdbcTemplate.update(QUERY_DELETE, namedParameters);
        result = (inserted > 0) ?  true : false;
        return result;
    }

    @Override
    public List<FullInfo> getFullInfoItems(int pageNumber, int itemsCountOnPage, String[] columnForOrderSpaceSortDirection, String filter, String filterColumn, Integer category) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        return null;
    }

    @Override
    public List<FullInfo> getAdvancedInfoItems(int pageNumber, int itemsCountOnPage, String[] columnForOrderSpaceSortDirection, Map<String, Object> filterMap, Integer category) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        ItemOrders ord = null;
        String column = null;
        String order = null;
        boolean isFilter = false;
        boolean isOrder = false;
        StringBuilder sb = new StringBuilder();
        if (category == null || category == 0) {
            sb.append("SELECT * FROM fullinfoitems ");
        } else {
            sb.append("SELECT * FROM("
                    + "SELECT * FROM fullinfoitems finfo WHERE finfo.category_id IN("
                    + "SELECT  cat_id FROM OFFERCATEGORY CONNECT BY PRIOR cat_id = parentid"
                    + " START WITH parentid = " + category
                    + ") UNION (SELECT * FROM fullinfoitems finfo"
                    + " WHERE finfo.category_id = " + category + "))");
        }
        String tmpsql = new String(sb.toString());
        boolean badFilter = false;
        if (!filterMap.isEmpty()) {
            sb.append(" WHERE ");
            int mapSize = 0;

            for (String columnName : filterMap.keySet()) {
                try {
                    ord = ItemOrders.valueOf(columnName.toUpperCase());
                    column = ord.toString();
                    if (column.equals("START_DATE")
                            || column.equals("STOP_DATE")) {
                        sb.append(String.format(DATE_VALUE, columnName));
                    } else {
                        if (column.equals("MIN_PRICE")) {
                            sb.append(String.format(PRICE_FILTER, ItemOrders.START_PRICE.toString(), ">"));
                        } else if (column.equals("MAX_PRICE")) {
                            sb.append(String.format(PRICE_FILTER, ItemOrders.START_PRICE.toString(), "<"));
                        } else {
                            sb.append(" " + column + " ");
                            sb.append(" LIKE :other");
                        }
                    }

                    if (mapSize < filterMap.size() - 1) {
                        mapSize++;
                        sb.append(" AND ");
                    }

                } catch (IllegalArgumentException e) {
                    column = null;
                    badFilter = true;
                    // needed delete where
                }

            }

            isFilter = true;

        }

        if (badFilter) {
            sb = new StringBuilder(tmpsql);
        }
        boolean badOrder = false;
        if (columnForOrderSpaceSortDirection != null) {
            if (columnForOrderSpaceSortDirection.length != 0) {
                // orderParams = new ArrayList<String>();
                isOrder = true;
                for (int i = 0; i < columnForOrderSpaceSortDirection.length; i++) {
                    StringTokenizer st = new StringTokenizer(
                            columnForOrderSpaceSortDirection[i]);
                    while (st.hasMoreTokens()) {
                        try {
                            ord = ItemOrders.valueOf(st.nextToken()
                                    .toUpperCase());
                            column = ord.toString();
                        } catch (IllegalArgumentException e) {
                            column = null;
                            badOrder = true;
                        }
                        if (st.hasMoreTokens()) {
                            if (st.nextToken().toUpperCase().equals("DESC")) {
                                order = "DESC";

                            } else {
                                order = "ASC";
                            }
                        }
                        if (column != null) {
                            if ((i + 1) < columnForOrderSpaceSortDirection.length) {
                                sb.append(",");
                            }
                        }
                    }

                }
                if (badOrder) {
                    sb = new StringBuilder(tmpsql);
                }
            }

        }
        if (isOrder) {
            sb.append(String.format(OREDER_DIRECTION, column, order));
        }
        String sql = sb.toString();
        sql = Paging.getPage(pageNumber, itemsCountOnPage, sql);
        if (isFilter) {
            for (String columnName : filterMap.keySet()) {
                if (columnName.equals("START_DATE")
                        || columnName.equals("STOP_DATE")) {
                    java.sql.Timestamp t = new Timestamp(
                            ((java.util.Date) filterMap.get(columnName))
                                    .getTime());
                    namedParameters.put("dateValue", t);
                } else if (columnName.equals("MIN_PRICE") || columnName.equals("MAX_PRICE")) {
                    namedParameters.put("price", filterMap.get(columnName));
                } else {
                    namedParameters.put("other", "%" + filterMap.get(columnName) + "%");
                }
            }
        }
        List<FullInfo> fullInfoList = namedParameterJdbcTemplate.query(sql, namedParameters, new FullInfoMapper());
        return fullInfoList;
    }

    @Override
    public List<FullInfo> getFullInfoMyItems(int pageNumber, int itemsCountOnPage, Integer userId) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM fullinfoitems "
                + "WHERE \"UID\" IN (SELECT distinct b.item_id FROM items itm "
                + "LEFT JOIN BIDS b ON itm.item_id = b.item_id "
                + "WHERE b.bidder_id  = :userId) "
                + "OR SELLER = (SELECT u.login FROM USERS u"
                + " WHERE u.user_id = :userId)");
        namedParameters.put("userId", userId.toString());
        String sql = sb.toString();
        sql = Paging.getPage(pageNumber, itemsCountOnPage, sql);
        List<FullInfo> fullInfoList = namedParameterJdbcTemplate.query(sql, namedParameters, new FullInfoMapper());
        return fullInfoList;
    }

    @Override
    public Map<Integer, Integer> getSoldItems() {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        //QUERY_SOLDITEMS
        return null;
    }

    @Override
    public boolean isInformationMessageSend(Integer itemId) {
        return false;
    }

    @Override
    public boolean markAsSendedMessage(Integer itemId) {
        return false;
    }

    @Override
    public List<History> getHistory(Integer itemId) {
        return null;
    }

    @Override
    public List<Category> getCategory() {
        return null;
    }

    @Override
    public boolean updateItem(Item to) {
        Map<String, Object> namedParameters = new HashMap<String, Object>();
        boolean result = false;
        namedParameters.put("title", to.getTitle());
        namedParameters.put("description", to.getDescription());
        namedParameters.put("startPrice", to.getStartPrice());
        namedParameters.put("timeLeft", to.getTimeLeft());
        if(to.isBuyItNow()){
            namedParameters.put("buyItNow", 1);
        } else {
            namedParameters.put("buyItNow", 0);
        }
        namedParameters.put("bidIncrement", to.getBidIncrement());
        namedParameters.put("itemId", to.getItemId());
        int inserted = namedParameterJdbcTemplate.update(QUERY_UPDATE, namedParameters);
        if (inserted > 0){
            result = true;
        }
        return result;
    }
}
