package com.epam.training.persistence.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Oleg_Burshinov
 * Transfer object for simple serach
 */
@Component
@JsonAutoDetect
public class SimpleSearch implements Serializable {
	/**
	 * serial uid
	 */
	private static final long serialVersionUID = -5374444528700805352L;
	private String keywordInput;
	private int shipping;
	/**
	 * Item ID
	 */
	public  final int UID = 1;
	/**
	 * Item title
	 */
	public  final int Title = 2;
	/**
	 * Item description
	 */
	public  final int Description = 3;
	/**
	 * @return the keywordInput
	 */
    @JsonProperty
	public String getKeywordInput() {
		return keywordInput;
	}
	/**
	 * @param keywordInput the keywordInput to set
	 */
	public void setKeywordInput(String keywordInput) {
		this.keywordInput = keywordInput;
	}
	/**
	 * @return the shipping
	 */
    @JsonProperty
	public int getShipping() {
		return shipping;
	}
	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(int shipping) {
		this.shipping = shipping;
	}
	
	
}
