package com.epam.training.persistence.service;

import com.epam.training.persistence.dao.ItemDao;
import com.epam.training.persistence.pojo.FullInfo;
import com.epam.training.persistence.pojo.Item;
import com.epam.training.persistence.pojo.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg_Burshinov on 20.01.14.
 */
@Service
public class ItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public ItemService() {
        super();
    }

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private UserContext userContext;

    public int create(Item to) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        to.setStartBiddingDate(calendar);
        return itemDao.insertItem(to);
    }

    public boolean update(Item to) {
        return itemDao.updateItem(to);
    }

    public Item read(Integer id) {
        return itemDao.findItemById(id);
    }

    public List<FullInfo> getAdvancedInfoItems(int pageNumber, int itemsCountOnPage, String[] columnForOrderSpaceSortDirection, Map<String, Object> filterMap, Integer category) {
        return itemDao.getAdvancedInfoItems(pageNumber, itemsCountOnPage, columnForOrderSpaceSortDirection, filterMap, category);
    }

    public List<FullInfo> getFullInfoMyItems(int pageNumber, int itemsCountOnPage, Integer userId) {
        return itemDao.getFullInfoMyItems(pageNumber, itemsCountOnPage, userId);
    }

    public boolean delete(Integer id) {
        Item item = read(id);
        if (item != null) {
            if (item.getSellerId().equals(userContext.getUser().getUserId())) {
                return itemDao.deleteItem(id);
            }
        }
            return false;
    }
}