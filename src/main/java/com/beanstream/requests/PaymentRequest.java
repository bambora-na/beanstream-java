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

/**
 *
 * @author bowens
 */
public abstract class PaymentRequest {
    private String merchant_id;
    private String order_number;
    private String amount;
    private String language;
    private String customer_ip;
    private String term_url;
    private String comments;

    //public BillingAddress billing;
    //public ShippingAddress shipping;
    //public Custom custom;
    
    public String getMerchant_id() {
        return merchant_id;
    }

    public PaymentRequest setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
        return this;
    }

    public String getOrder_number() {
        return order_number;
    }

    public PaymentRequest setOrder_number(String order_number) {
        this.order_number = order_number;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public PaymentRequest setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public PaymentRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCustomer_ip() {
        return customer_ip;
    }

    public PaymentRequest setCustomer_ip(String customer_ip) {
        this.customer_ip = customer_ip;
        return this;
    }

    public String getTerm_url() {
        return term_url;
    }

    public PaymentRequest setTerm_url(String term_url) {
        this.term_url = term_url;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public PaymentRequest setComments(String comments) {
        this.comments = comments;
        return this;
    }
    
    
}
