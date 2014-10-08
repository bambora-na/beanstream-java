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

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Credit Card.
 * 
 * @author bowens
 */
public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private String number;
    @SerializedName("expiry_month")
    private String expiryMonth;
    @SerializedName("expiry_year")
    private String expiryYear;
    private String cvd;
    private boolean complete = true; // false for pre-authorizations

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public Card setName(String name) {
        this.name = name;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Card setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public Card setExpiryMonth(String expiry_month) {
        this.expiryMonth = expiry_month;
        return this;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public Card setExpiryYear(String expiry_year) {
        this.expiryYear = expiry_year;
        return this;
    }

    public String getCvd() {
        return cvd;
    }

    public Card setCvd(String cvd) {
        this.cvd = cvd;
        return this;
    }

    public boolean isComplete() {
        return complete;
    }

    public Card setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    
    
}
