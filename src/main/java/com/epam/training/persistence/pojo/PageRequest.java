package com.epam.training.persistence.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleg_Burshinov on 24.01.14.
 */
@Component
@JsonAutoDetect
public class PageRequest {
    private Integer pageNumber;
    private Integer itemsCountOnPage;
    private String[] order;
    private Map<String, Object> filter;
    private boolean myitems;
    private SimpleSearch search;
    private AdvancedSearch advancedSearch;
    private Integer category;

    public PageRequest() {
        super();
    }

    @JsonProperty
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @JsonProperty
    public Integer getItemsCountOnPage() {
        return itemsCountOnPage;
    }

    public void setItemsCountOnPage(Integer itemsCountOnPage) {
        this.itemsCountOnPage = itemsCountOnPage;
    }

    @JsonProperty
    public String[] getOrder() {
        return order;
    }

    public void setOrder(String[] order) {
        this.order = order;
    }

    @JsonProperty
    public Map<String, Object> getFilter() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (advancedSearch.getItemUID() != null) {
            if(advancedSearch.getItemUID() > 0){
                map.put("UID", advancedSearch.getItemUID().toString());
            }
        }
        if (advancedSearch.getTitleOfItem() != null
                && !advancedSearch.getTitleOfItem().equals("")) {
            map.put("TITLE", advancedSearch.getTitleOfItem());
        }
        if (advancedSearch.getDescription() != null
                && !advancedSearch.getDescription().equals("")) {
            map.put("DESCRIPTION", advancedSearch.getDescription());
        }
        if (advancedSearch.getMinPrice() > 0) {
            map.put("MIN_PRICE", advancedSearch.getMinPrice());
        }
        if (advancedSearch.getMaxPrice() > 0) {
            map.put("MAX_PRICE", advancedSearch.getMaxPrice());
        }
        if (advancedSearch.isBuyItNow()) {
            map.put("BUYITNOW", 1);
        }
        if (advancedSearch.getStartDate() != null) {
            map.put("START_DATE", advancedSearch.getStartDate());
        }
        if (advancedSearch.getExpireDate() != null) {
            map.put("STOP_DATE", advancedSearch.getExpireDate());
        }
        if (advancedSearch.getBiddersCount() != null) {
            if (advancedSearch.getBiddersCount() > 0){
                map.put("BIDDER_COUNT", advancedSearch.getBiddersCount().toString());
            }
        }
        Map<String, Object> synmap = Collections.synchronizedMap(map);
        return synmap;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    @JsonProperty
    public boolean isMyitems() {
        return myitems;
    }

    public void setMyitems(boolean myitems) {
        this.myitems = myitems;
    }

    @JsonProperty
    public SimpleSearch getSearch() {
        return search;
    }

    @JsonSetter
    public void setSearch(SimpleSearch search) {
        this.search = search;
    }

    @JsonProperty
    public AdvancedSearch getAdvancedSearch() {
        return advancedSearch;
    }
    @JsonSetter
    public void setAdvancedSearch(AdvancedSearch advancedSearch) {
        this.advancedSearch = advancedSearch;
    }


    @JsonProperty
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageRequest that = (PageRequest) o;

        if (myitems != that.myitems) return false;
        if (advancedSearch != null ? !advancedSearch.equals(that.advancedSearch) : that.advancedSearch != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (filter != null ? !filter.equals(that.filter) : that.filter != null) return false;
        if (!itemsCountOnPage.equals(that.itemsCountOnPage)) return false;
        if (!Arrays.equals(order, that.order)) return false;
        if (!pageNumber.equals(that.pageNumber)) return false;
        if (search != null ? !search.equals(that.search) : that.search != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageNumber.hashCode();
        result = 31 * result + itemsCountOnPage.hashCode();
        result = 31 * result + (order != null ? Arrays.hashCode(order) : 0);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (myitems ? 1 : 0);
        result = 31 * result + (search != null ? search.hashCode() : 0);
        result = 31 * result + (advancedSearch != null ? advancedSearch.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNumber=" + pageNumber +
                ", itemsCountOnPage=" + itemsCountOnPage +
                ", order=" + Arrays.toString(order) +
                ", filter=" + filter +
                ", myitems=" + myitems +
                ", search=" + search +
                ", advancedSearch=" + advancedSearch +
                ", category=" + category +
                '}';
    }
}
