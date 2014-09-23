package com.beanstream;

import com.beanstream.connection.HttpMethod;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.CashPaymentRequest;
import com.beanstream.requests.ChequePaymentRequest;
import com.beanstream.requests.LegatoTokenRequest;
import com.beanstream.requests.TokenPaymentRequest;
import com.beanstream.responses.BeanstreamResponse;
import com.beanstream.responses.LegatoTokenResponse;
import com.beanstream.responses.PaymentResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Example code.
 * 
 * @author bowens
 */
public class SampleTransactions {

	public static void main(String[] args) {
		SampleTransactions t = new SampleTransactions();
        t.testPayment();
		t.testVoidPayment();
        t.testPreAuthorization();
	}

	private AtomicInteger sequence = new AtomicInteger(1);

	private String getRandomOrderId(String prefix) {
		String orderId = null;
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		if (prefix != null) {
			sb.append(prefix);
			sb.append("_");
		}
		SimpleDateFormat df = new SimpleDateFormat("MMkkmmssSSSS");
		sb.append(sequence.getAndIncrement());
		sb.append("_");
		sb.append(df.format(date));
		orderId = sb.toString();
		if (orderId.length() > 30) {
			orderId = orderId.substring(0, 29);
		}
		return orderId;
	}

	private void testVoidPayment() {

		Gateway beanstream = new Gateway("v1", 300200578,
				"4BaD82D9197b4cc4b70a221911eE9f70");

		CardPaymentRequest paymentRequest = new CardPaymentRequest();
		paymentRequest.setAmount("90.00");
		paymentRequest.setMerchantId("300200578");
		paymentRequest.setOrderNumber(getRandomOrderId("PEDRO"));
		paymentRequest.getCard().setName("John Doe")
				.setNumber("5100000010001004").setExpiryMonth("12")
				.setExpiryYear("18").setCvd("123");

		try {
			PaymentResponse response = beanstream.payments().makePayment(
					paymentRequest);
			if (response.isApproved()) {
				Gson gsonpp = new GsonBuilder().setPrettyPrinting().create();
				System.out
						.println("Your Payment has been approved response: \n"
								+ gsonpp.toJson(response));

				response = beanstream.payments()
						.voidPayment(response.id, 90.00);

				if ("VP".equals(response.type)) {
					System.out.println("The payment was voided");

				} else {
					System.out.println("The payment was not voided");
				}
			}
		} catch (BeanstreamApiException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"An error occurred", ex);
		}

	}

	public void testPreAuthorization() {

		Gateway beanstream = new Gateway("v1", 300200578,
				"4BaD82D9197b4cc4b70a221911eE9f70");

		CardPaymentRequest paymentRequest = new CardPaymentRequest();
		paymentRequest.setAmount("90.00");
		paymentRequest.setMerchantId("300200578");
		paymentRequest.setOrderNumber(getRandomOrderId("GAS"));
		paymentRequest.getCard().setName("John Doe")
				.setNumber("5100000010001004").setExpiryMonth("12")
				.setExpiryYear("18").setCvd("123");

		try {
			PaymentResponse response = beanstream.payments().preAuth(
					paymentRequest);
			if (response.isApproved()) {

				PaymentResponse authResp = beanstream.payments()
						.preAuthCompletion(response.id, 43.50, null);
				if (!authResp.isApproved()) {
					Assert.fail("This auth completion should be approved because a greater amount has been pre authorized");
				}
			}
		} catch (BeanstreamApiException ex) {
			System.out.println(BeanstreamResponse.fromException(ex));

		}
    }

    private void testPayment() {

        Gateway beanstream = new Gateway("v1", 300200578, "4BaD82D9197b4cc4b70a221911eE9f70");
        HttpsConnector connector = new HttpsConnector(300200578, "4BaD82D9197b4cc4b70a221911eE9f70");

        /* Test Card Payment */
        CardPaymentRequest req = new CardPaymentRequest();
        req.setAmount("100.00");
        req.setMerchantId("300200578");
        req.setOrderNumber( getRandomOrderId("test") );
        req.getCard().setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {

            PaymentResponse response = beanstream.payments().makePayment(req);
            System.out.println("Card Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }

        /* Test Cash Payment */
        CashPaymentRequest cashReq = new CashPaymentRequest();
        cashReq.setAmount("123.45");
        cashReq.setMerchantId("300200578");
        cashReq.setOrderNumber( getRandomOrderId("cash") );

        try {

            PaymentResponse response = beanstream.payments().makePayment(cashReq);
            System.out.println("Cash Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }

        /* Test Cheque Payment */
        ChequePaymentRequest chequeReq = new ChequePaymentRequest();
        chequeReq.setAmount("668.99");
        chequeReq.setMerchantId("300200578");
        chequeReq.setOrderNumber( getRandomOrderId("cheque") );

        try {

            PaymentResponse response = beanstream.payments().makePayment(chequeReq);
            System.out.println("Cheque Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }

        /* Test Token Payment */
        // The first step is to call the Legato service to get a token.
        // This is normally performed on the client machine, and not on the server.
        // The goal with tokens is to not have credit card information move through your server,
        // thus lowering your scope for PCI compliance
        LegatoTokenRequest tokenRequest = new LegatoTokenRequest();
        tokenRequest.number = "5100000010001004";
        tokenRequest.expiryMonth = 12;
        tokenRequest.expiryYear = 18;
        tokenRequest.cvd = "123";

        String url = "https://www.beanstream.com/scripts/tokenization/tokens";
        String output = "";
        try {
            output = connector.ProcessTransaction(HttpMethod.post, url, tokenRequest);
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(SampleTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Parse the output and return a token response to get the token for the payment request
        Gson gson = new Gson();
        LegatoTokenResponse tokenResponse = gson.fromJson(output, LegatoTokenResponse.class);

        System.out.println("token: " + output);

        TokenPaymentRequest tokenReq = new TokenPaymentRequest();
        tokenReq.setAmount("100.00");
        tokenReq.setMerchantId("300200578");
        tokenReq.setOrderNumber( getRandomOrderId("token") );
        tokenReq.getToken().setName("John Doe")
                .setCode(tokenResponse.token)
                .setFunction("12");

        try {
            PaymentResponse response = beanstream.payments().makePayment(tokenReq);
            System.out.println("Token Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
        }

	}
    
}
