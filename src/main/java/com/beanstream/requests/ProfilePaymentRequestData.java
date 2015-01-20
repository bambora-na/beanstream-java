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

import com.google.gson.annotations.SerializedName;

/**
 * The data holding the payment profile information for making a purchase or pre-auth.
 * 
 * @author bowens
 */
public class ProfilePaymentRequestData {
    private boolean complete = true; // false for pre-authorizations
    @SerializedName("customer_code")
    private String customerCode;
    @SerializedName("card_id")
    private int cardId;
    
    public boolean isComplete() {
        return complete;
    }

    public ProfilePaymentRequestData setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public ProfilePaymentRequestData setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    public int getCardId() {
        return cardId;
    }

    public ProfilePaymentRequestData setCardId(int cardId) {
        this.cardId = cardId;
        return this;
    }
}
