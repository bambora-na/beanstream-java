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
 * Parameters to complete Interac payment.
 * @author ilya
 */
public class InteracResponse {
	private String funded;
	
    @SerializedName("idebit_track2")
    private String idebitTrack2;

    @SerializedName("idebit_isslang")
    private String idebitIsslang;

    @SerializedName("idebit_version")
    private String idebitVersion;

    @SerializedName("idebit_issconf")
    private String idebitIssconf;

    @SerializedName("idebit_issname")
    private String idebitIssname;

    @SerializedName("idebit_amount")
    private String idebitAmount;

    @SerializedName("idebit_invoice")
    private String idebitInvoice;

	public String getFunded() {
		return funded;
	}

	public void setFunded( String funded ) {
        if (funded != null && funded.length() > 20)
            throw new IllegalArgumentException("Funded cannot be longer than 20 characters!");
		this.funded = funded;
	}

	public String getIdebitTrack2() {
		return idebitTrack2;
	}

	public void setIdebitTrack2( String idebitTrack2 ) {
        if (idebitTrack2 != null && idebitTrack2.length() > 256)
            throw new IllegalArgumentException("IdebitTrack2 cannot be longer than 256 characters!");
		this.idebitTrack2 = idebitTrack2;
	}

	public String getIdebitIsslang() {
		return idebitIsslang;
	}

	public void setIdebitIsslang( String idebitIsslang ) {
        if (idebitIsslang != null && idebitIsslang.length() > 2)
            throw new IllegalArgumentException("IdebitIsslang cannot be longer than 2 characters!");
		this.idebitIsslang = idebitIsslang;
	}

	public String getIdebitVersion() {
		return idebitVersion;
	}

	public void setIdebitVersion( String idebitVersion ) {
        if (idebitVersion != null && idebitVersion.length() > 1)
            throw new IllegalArgumentException("IdebitVersion cannot be longer than 1 character!");
		this.idebitVersion = idebitVersion;
	}

	public String getIdebitIssconf() {
		return idebitIssconf;
	}

	public void setIdebitIssconf( String idebitIssconf ) {
        if (idebitIssconf != null && idebitIssconf.length() > 32)
            throw new IllegalArgumentException("IdebitIssconf cannot be longer than 32 characters!");
		this.idebitIssconf = idebitIssconf;
	}

	public String getIdebitIssname() {
		return idebitIssname;
	}

	public void setIdebitIssname( String idebitIssname ) {
        if (idebitIssname != null && idebitIssname.length() > 32)
            throw new IllegalArgumentException("IdebitIssname cannot be longer than 32 characters!");
		this.idebitIssname = idebitIssname;
	}

	public String getIdebitAmount() {
		return idebitAmount;
	}

	public void setIdebitAmount( String idebitAmount ) {
        if (idebitAmount != null && idebitAmount.length() > 9)
            throw new IllegalArgumentException("IdebitAmount cannot be longer than 9 characters!");
		this.idebitAmount = idebitAmount;
	}

	public String getIdebitInvoice() {
		return idebitInvoice;
	}

	public void setIdebitInvoice( String idebitInvoice ) {
        if (idebitInvoice != null && idebitInvoice.length() > 20)
            throw new IllegalArgumentException("IdebitInvoice cannot be longer than 20 characters!");
		this.idebitInvoice = idebitInvoice;
	}
}
