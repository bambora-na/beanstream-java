package com.beanstream.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class PaymentProfile {

	@SerializedName("customer_code")
	private String id;
	private Card card;
	private Address billing;
	private CustomFields custom;
	private String language;
	private String comments;
	@SerializedName("modified_date")
	private Date modifiedDate;
	@SerializedName("last_transaction")
	private String lastTransaction;
	private String status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public Address getBilling() {
		return billing;
	}
	public void setBilling(Address billing) {
		this.billing = billing;
	}
	public CustomFields getCustom() {
		return custom;
	}
	public void setCustom(CustomFields custom) {
		this.custom = custom;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getLastTransaction() {
		return lastTransaction;
	}
	public void setLastTransaction(String lastTransaction) {
		this.lastTransaction = lastTransaction;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
