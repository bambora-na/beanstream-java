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
package com.beanstream.connection;

import java.text.MessageFormat;

/**
 * Common URLs to connect to the Beanstream servers and API services.
 * 
 * @author bowens
 */
public class BeanstreamUrls {
	public static final String BaseUrl = "https://{0}.beanstream.com/api";
	public static final String BasePaymentsUrl = BaseUrl + "/{1}/payments";
    public static final String GetPaymentUrl =  BasePaymentsUrl + "/{2}";
	public static final String BaseProfilesUrl = BaseUrl + "/{1}/profiles";
	public static final String PreAuthCompletionsUrl = BasePaymentsUrl
			+ "/{2}/completions";
	public static final String ReturnsUrl = BasePaymentsUrl + "/{2}/returns";
        
	public static final String VoidsUrl = BasePaymentsUrl + "/{2}/void";
	public static final String ContinuationsUrl = BasePaymentsUrl
			+ "/{2}/continue";
	public static final String ProfileUri = "/{id}";
	public static final String CardsUri = ProfileUri + "/cards";

	public static String getPaymentUrl(String platform, String version) {
		return MessageFormat.format(BeanstreamUrls.BasePaymentsUrl, platform,
				version);
	}

	public static String getPreAuthCompletionsUrl(String platform,
			String version, String paymentId) {
		return MessageFormat.format(PreAuthCompletionsUrl, platform, version,
				paymentId);
	}

	public static String getVoidPaymentUrl(String platform, String version,
			String paymentId) {
		return MessageFormat.format(BeanstreamUrls.VoidsUrl, platform, version,
				paymentId);
	}
        
    public static String getReturnUrl(String platform,
        String version, String paymentId){
            return MessageFormat.format(BeanstreamUrls.ReturnsUrl, platform, version,
            paymentId);
    }

    public static String getUnreferencedReturnUrl(String platform,
        String version){
            return MessageFormat.format(BeanstreamUrls.ReturnsUrl, platform, version, "0");
    }
    
    public static String getPaymentUrl(String platform, String version, String paymentId) {
		return MessageFormat.format(BeanstreamUrls.GetPaymentUrl, platform, version,
				paymentId);
	}
}
