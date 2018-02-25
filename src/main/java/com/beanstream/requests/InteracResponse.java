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
    @SerializedName("idebit_track2")
    public String idebitTrack2;

    @SerializedName("idebit_isslang")
    public String idebitIsslang;

    @SerializedName("idebit_version")
    public String idebitVersion;

    @SerializedName("idebit_issconf")
    public String idebitIssconf;

    @SerializedName("idebit_issname")
    public String idebitIssname;

    @SerializedName("idebit_amount")
    public String idebitAmount;

    @SerializedName("idebit_invoice")
    public String idebitInvoice;

	public String getIdebitTrack2() {
		return idebitTrack2;
	}

	public void setIdebitTrack2( String idebitTrack2 ) {
		this.idebitTrack2 = idebitTrack2;
	}

	public String getIdebitIsslang() {
		return idebitIsslang;
	}

	public void setIdebitIsslang( String idebitIsslang ) {
		this.idebitIsslang = idebitIsslang;
	}

	public String getIdebitVersion() {
		return idebitVersion;
	}

	public void setIdebitVersion( String idebitVersion ) {
		this.idebitVersion = idebitVersion;
	}

	public String getIdebitIssconf() {
		return idebitIssconf;
	}

	public void setIdebitIssconf( String idebitIssconf ) {
		this.idebitIssconf = idebitIssconf;
	}

	public String getIdebitIssname() {
		return idebitIssname;
	}

	public void setIdebitIssname( String idebitIssname ) {
		this.idebitIssname = idebitIssname;
	}

	public String getIdebitAmount() {
		return idebitAmount;
	}

	public void setIdebitAmount( String idebitAmount ) {
		this.idebitAmount = idebitAmount;
	}

	public String getIdebitInvoice() {
		return idebitInvoice;
	}

	public void setIdebitInvoice( String idebitInvoice ) {
		this.idebitInvoice = idebitInvoice;
	}
}
