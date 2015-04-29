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
package com.beanstream.requests;

import com.beanstream.domain.Address;
import com.beanstream.domain.CustomFields;
import com.google.gson.annotations.SerializedName;

/**
 * A request to make a payment. Specific payment requests need to subclass this
 * abstract class. Example payments: Card, Cash, Cheque, Token.
 * 
 * @author bowens
 */
public abstract class PaymentRequest {
    
    @SerializedName("merchant_id")
    private String merchant_id;
    @SerializedName("order_number")
    private String orderNumber;
    private String amount;
    private String language;
    @SerializedName("customer_ip")
    private String customerIp;
    @SerializedName("term_url")
    private String termUrl;
    private String comments;

    public Address billing;
    public Address shipping;
    public CustomFields custom;
    
    public String getMerchantId() {
        return merchant_id;
    }

    public PaymentRequest setMerchantId(String merchant_id) {
        this.merchant_id = merchant_id;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public PaymentRequest setOrderNumber(String order_number) {
        this.orderNumber = order_number;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public PaymentRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }
    
    public PaymentRequest setAmount(double amount) {
        this.amount = ""+amount;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public PaymentRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public PaymentRequest setCustomerIp(String customer_ip) {
        this.customerIp = customer_ip;
        return this;
    }

    public String getTermUrl() {
        return termUrl;
    }

    public PaymentRequest setTermUrl(String term_url) {
        this.termUrl = term_url;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public PaymentRequest setComments(String comments) {
        this.comments = comments;
        return this;
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

    public CustomFields getCustom() {
        return custom;
    }

    public void setCustom(CustomFields custom) {
        this.custom = custom;
    }
    
    
}
