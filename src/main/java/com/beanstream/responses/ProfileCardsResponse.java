package com.beanstream.responses;

import java.util.ArrayList;
import java.util.List;

import com.beanstream.domain.Card;
import com.google.gson.annotations.SerializedName;

public class ProfileCardsResponse extends ProfileResponse {
	@SerializedName("card")
	List<Card> cards;
	

	public List<Card> getCards() {
		return (cards != null ? cards : new ArrayList<Card>());
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
