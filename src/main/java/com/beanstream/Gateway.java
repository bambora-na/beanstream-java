package com.beanstream;

import org.apache.http.HttpStatus;

import com.beanstream.api.PaymentsAPI;
import com.beanstream.api.ProfilesAPI;
import com.beanstream.api.ReportingAPI;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.responses.BeanstreamResponse;
import org.apache.http.client.HttpClient;

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

/**
 * The main entry point to process payments, create payment profiles, and run
 * reports on payments.
 * 
 * @author bowens
 */
public class Gateway {

	private Configuration config;
	private PaymentsAPI paymentsApi;
	private ReportingAPI reportingApi;

	public Gateway(String version, int merchantId, String apiKeyPayments) {
		config = new Configuration(merchantId, apiKeyPayments);
		config.setVersion(version);
	}

	public Gateway(String version, int merchantId, String apiKeyPayments,
			String apiKeyProfiles) {
		config = new Configuration(merchantId, apiKeyPayments);
		config.setProfilesApiPasscode(apiKeyProfiles);
		config.setVersion(version);
	}

	public Gateway(String version, int merchantId, String apiKeyPayments,
			String apiKeyProfiles, String apiKeyReporting) {
		config = new Configuration(merchantId, apiKeyPayments);
		config.setProfilesApiPasscode(apiKeyProfiles);
		config.setReportingApiPasscode(apiKeyReporting);
		config.setVersion(version);
	}

	public Configuration getConfiguration() {
		return config;
	}

	public void setConfiguration(Configuration config) {
		this.config = config;
	}

        /**
         * This allows you to specify your own HttpClient with its own connection
         * parameters such as connection timeouts.
         * @param httpClient 
         */
        public void setCustomHttpsClient(HttpClient httpClient) {
            this.config.setCustomHttpClient(httpClient);
        }
        
	/**
	 * Process payments, pre-auth's, void payments, return payments. Handle
	 * credit cards, cash, cheque, and tokenized payments.
	 * 
	 * @return The API class that does the payment magic
	 */
	public PaymentsAPI payments() {
		return getPaymentApi();
	}

	/**
	 * Get a transaction or search for a range of transactions with the Reports
	 * API.
	 * 
	 * @return API that gives you access to all previous transactions.
	 */
	public ReportingAPI reports() {
		return getReportingApi();
	}

	public ProfilesAPI profiles() {
		return getProfilesApi();
	}

	private ProfilesAPI profilesApi;

	private ProfilesAPI getProfilesApi() {
		if (profilesApi == null) {
			profilesApi = new ProfilesAPI(config);
		}
		return profilesApi;
	}

	private PaymentsAPI getPaymentApi() {
		if (paymentsApi == null) {
			paymentsApi = new PaymentsAPI(config);
		}
		return paymentsApi;
	}

	public void setPaymentsApi(PaymentsAPI api) {
		this.paymentsApi = api;
	}

	private ReportingAPI getReportingApi() {
		if (reportingApi == null)
			reportingApi = new ReportingAPI(config);
		return reportingApi;
	}

	public void setReportingApi(ReportingAPI api) {
		this.reportingApi = api;
	}

	public static void assertNotEmpty(String value, String errorMessage)
            throws BeanstreamApiException {
        // could use StringUtils.assertNotNull();
        if (value == null || value.trim().isEmpty()) {
            // TODO - do we need to supply category and code ids here?
            BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid payment request");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
        }
    }
	public static void assertNotNull(Object value, String errorMessage)
            throws BeanstreamApiException {
        // could use StringUtils.assertNotNull();
        if (value == null) {
            // TODO - do we need to supply category and code ids here?
            BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid payment request");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
        }
    }
}
