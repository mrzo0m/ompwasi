/**
 * 
 */
package com.epam.training.persistence.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Oleg_Burshinov
 *
 */
@Component
@JsonAutoDetect
public class Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2370391546618485481L;

    /**
     * Unique id seller
     */
    private Integer sellerId;
    /**
     * Title
     */
    @Size(min = 1, max = 25)
    private String title;
    /**
     * About item
     */
    @Size(min = 1, max = 70)
    private String description;
    /**
     * Start price
     */
    @NotNull
    private Double startPrice;
    /**
     * How many time to by this item
     */



    private Integer bidIncrement;
    private  boolean buyItNow;
    private  Integer timeLeft;
    private Calendar    startBiddingDate;
    private Integer itemId;

    private Integer category;

    public Item() {
        super();
    }

    /**
     * @param sellerId
     * @param title
     * @param description
     * @param startPrice
     * @param timeLeft
     * @param startBiddingDate
     * @param buyItNow
     * @param bidIncrement
     * @param itemId
     * @param category
     */
    public Item(Integer sellerId, String title, String description,
                double startPrice, Integer timeLeft, Calendar startBiddingDate,
                boolean buyItNow, Integer bidIncrement, Integer itemId,
                Integer category) {
        super();
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.timeLeft = timeLeft;
        this.startBiddingDate = startBiddingDate;
        this.buyItNow = buyItNow;
        this.bidIncrement = bidIncrement;
        this.itemId = itemId;
        this.category = category;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Integer getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(Integer bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public boolean isBuyItNow() {
        return buyItNow;
    }

    public void setBuyItNow(boolean buyItNow) {
        this.buyItNow = buyItNow;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Calendar getStartBiddingDate() {
        return startBiddingDate;
    }

    public void setStartBiddingDate(Calendar startBiddingDate) {
        this.startBiddingDate = startBiddingDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" +
                "sellerId=" + sellerId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", bidIncrement=" + bidIncrement +
                ", buyItNow=" + buyItNow +
                ", timeLeft=" + timeLeft +
                '}';
    }
	
}
