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
 * The response for interac payment request. 
 * Before returning the response to your users HTML client, you will need to save 
 * the merchant_data string in the users session. This value can be used as the {id} value 
 * when creating your 'continue' endpoint URL for the final request in step 3.
 * The response will have HTML in the contents. This should be embedded in the user's 
 * browser client and this needs to be displayed to the customer to redirect them to the 
 * Interac login page. Here the customer will log onto their bank account and approve the payment. 
 * An approved or declined payment will forward the customer back to the 
 * Funded or Non-funded URLs (respectively) on your website.
 * 
 * @author ilya
 */
public class InteracPaymentResponse {
    @SerializedName("merchant_data")
    public String merchantData;

    public String contents;

    public Link[] links;
}
