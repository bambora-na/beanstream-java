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
package com.beanstream.api;

import java.text.MessageFormat;

import com.beanstream.Configuration;
import com.beanstream.connection.BeanstreamUrls;
import com.beanstream.connection.HttpMethod;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.responses.PaymentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The entry point for processing payments.
 * 
 * @author bowens
 */
public class PaymentsAPI {

	private Configuration config;
	private Gson gson = new Gson();

	public PaymentsAPI(Configuration config) {
		this.config = config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public PaymentResponse makePayment(CardPaymentRequest paymentRequest)
			throws BeanstreamApiException {
		paymentRequest.setMerchant_id("" + config.getMerchantId());
		paymentRequest.getCard().setComplete(true); // false for pre-auth

		HttpsConnector connector = new HttpsConnector(config.getMerchantId(),
				config.getApiPasscode());

		// build the URL
		String url = MessageFormat.format(BeanstreamUrls.BasePaymentsUrl,
				config.getPlatform(), config.getVersion());

		// process the transaction using the REST API
		String response = connector.ProcessTransaction(HttpMethod.post, url,
				paymentRequest);

		// parse the output and return a PaymentResponse
		Gson gson = new Gson();
		return gson.fromJson(response, PaymentResponse.class);
	}

	/**
	 * Void the specified paymentId. Voids generally need to occur before end of
	 * business on the same day that the transaction was processed. Voids are
	 * used to cancel a transaction before the item is registered against a
	 * customer credit card account. Card holders will never see a voided
	 * transaction on their credit card statement. As a result, voids can only
	 * be attempted on the same day as the original transaction. After the end
	 * of day (roughly 11:59 PM EST/EDT), void requests will be rejected from
	 * the API if attempted.
	 * 
	 * @author Pedro Garcia
	 * @param paymentId
	 *            payment transaction id to void
	 * @param amount
	 *            the amount to avoid in this transaction
	 * @return PaymentResponse as result you will received a payment response
	 *         with the same payment transaction id but with the type 'VP'
	 * @throws BeanstreamApiException
	 *             as a result of a business logic validation or any other error @see
	 */
	public PaymentResponse voidPayment(String paymentId, double amount)
			throws BeanstreamApiException {

		// this should be injected to avoid creating one instance every single
		// request
		HttpsConnector connector = new HttpsConnector(config.getMerchantId(),
				config.getApiPasscode());

		assertNotEmpty(paymentId, "invalid paymentId");
		String url = MessageFormat.format(BeanstreamUrls.VoidsUrl,
				config.getPlatform(), config.getVersion(), paymentId);

		JsonObject voidRequest = new JsonObject();
		voidRequest.addProperty("merchant_id",
				String.valueOf(config.getMerchantId()));
		voidRequest.addProperty("amount", String.valueOf(amount));

		String response = connector.ProcessTransaction(HttpMethod.post, url,
				voidRequest);

		// parse the output and return a PaymentResponse
		return gson.fromJson(response, PaymentResponse.class);

	}

	private void assertNotEmpty(String value, String errorMessage)
			throws BeanstreamApiException {
		// could use StringUtils.assertNotNull();
		if (value == null || value.toString().trim().isEmpty()) {
			// throw a bad request
			throw new BeanstreamApiException(400, errorMessage);
		}
	}
}
