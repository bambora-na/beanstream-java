package com.beanstream.requests;

import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.CustomFields;
import com.beanstream.domain.Token;

public class ProfileRequest {

	private Card card;
	private Token token;
	private Address billing;
	private CustomFields custom;
	private String language;
	private String comments;
	
	public ProfileRequest() {
		super();
	}

	public ProfileRequest(Card card, Token token, Address billing,
			CustomFields custom, String language, String comments) {
		super();
		this.card = card;
		this.token = token;
		this.billing = billing;
		this.custom = custom;
		this.language = language;
		this.comments = comments;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	public ProfileRequest card(Card card) {
		this.card = card;
		return this;
	}

	public Address getBilling() {
		return billing;
	}

	public void setBilling(Address billing) {
		this.billing = billing;
	}
	
	public ProfileRequest billing(Address billing){
		this.billing = billing;
		return this;
	}

	public CustomFields getCustom() {
		return custom;
	}

	public void setCustom(CustomFields custom) {
		this.custom = custom;
	}
	
	public ProfileRequest custom(CustomFields custom){
		setCustom(custom);
		return this;
	}
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	public ProfileRequest language(String language){
		setLanguage(language);
		return this;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		if (comments != null && comments.trim().length() > 256) {
			comments = comments.substring(0, 256);
		}
		this.comments = comments;
	}
	public ProfileRequest comments(String comments){
		setComments(comments);
		return this;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
	
	
	
}
