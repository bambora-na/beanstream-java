package com.beanstream.domain;

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
	private String modifiedDateStr;
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
	public String getModifiedDateStr() {
		return modifiedDateStr;
	}
	public void setModifiedDateStr(String modifiedDateStr) {
		this.modifiedDateStr = modifiedDateStr;
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
	@Override
	public String toString() {
		return "PaymentProfile [id=" + id + ", card=" + card + ", billing="
				+ billing + ", custom=" + custom + ", language=" + language
				+ ", comments=" + comments + ", modifiedDateStr="
				+ modifiedDateStr + ", lastTransaction=" + lastTransaction
				+ ", status=" + status + "]";
	}
	
}
