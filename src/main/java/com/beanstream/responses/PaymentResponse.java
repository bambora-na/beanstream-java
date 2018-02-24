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
package com.beanstream.responses;

import com.google.gson.annotations.SerializedName;

/**
 * The response from a payment request. Use isApproved() to check if the
 * request was successful and approved, signaling that you received the funds.
 * 
 * @author bowens
 */
public class PaymentResponse {
    public String id;

    public String approved;

    @SerializedName("message_id")
    public String messageId;

    public String message;

    @SerializedName("auth_code")
    public String authCode;

    public String created;

    @SerializedName("order_number")
    public String orderNumber;

    public String type;

    @SerializedName("payment_method")
    public String paymentMethod;

    public CardResponse card;

    @SerializedName("interac_online")
    public InteracOnline interacOnline;

    public Link[] links;
    
    public boolean isApproved() {
        return "1".equals(approved);
    }
}
