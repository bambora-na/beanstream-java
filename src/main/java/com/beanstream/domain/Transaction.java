/* The MIT License (MIT)
 *
 * Copyright (c) 2014 Beanstream Internet Commerce Corp, Digital River, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.beanstream.domain;

import com.beanstream.responses.Link;
import java.util.List;

/**
 * Represents a single transaction with any adjustments that were performed
 * on it (return, void etc).
 * 
 * This is returned from the ReportingAPI class when you query for a single transaction.
 * 
 * @author bowens
 */
public class Transaction {
    
    private String id;

    private String approved;

    private String messageId;

    private String message;

    private String authCode;

    private String created;

    private String amount;

    private String orderNumber;

    private String type;

    private String comments;

    private String batchNumber;

    private String totalRefunds;

    private String totalCompletions;

    private String paymentMethod;

    private Card card;

    private Address billing;

    private Address shipping;

    private CustomFields customFields;

    private List<Adjustment> adjustments;

    private List<Link> links;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getTotalRefunds() {
		return totalRefunds;
	}

	public void setTotalRefunds(String totalRefunds) {
		this.totalRefunds = totalRefunds;
	}

	public String getTotalCompletions() {
		return totalCompletions;
	}

	public void setTotalCompletions(String totalCompletions) {
		this.totalCompletions = totalCompletions;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	public Address getShipping() {
		return shipping;
	}

	public void setShipping(Address shipping) {
		this.shipping = shipping;
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	public List<Adjustment> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<Adjustment> adjustments) {
		this.adjustments = adjustments;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
    
}
