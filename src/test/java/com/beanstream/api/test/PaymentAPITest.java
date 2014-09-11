package com.beanstream.api.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import com.beanstream.Gateway;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.responses.PaymentResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PaymentAPITest {

	private AtomicInteger sequence = new AtomicInteger(1);
	Gateway beanstream = new Gateway("v1", 300200578,
			"4BaD82D9197b4cc4b70a221911eE9f70");

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

	@Test()
	public void voidPayment() throws BeanstreamApiException {

		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("PEDRO"), "300200578", "90.00");

		PaymentResponse response = beanstream.payments().makePayment(
				paymentRequest);
		if (response.isApproved()) {
			response = beanstream.payments().voidPayment(response.id, 90.00);
			Assert.assertTrue("void payment response is not VP",
					"VP".equals(response.type));
		} else {
			Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
		}
	}

	private CardPaymentRequest getCreditCardPaymentRequest(String orderId,
			String merchantId, String amount) {
		CardPaymentRequest paymentRequest = new CardPaymentRequest();
		paymentRequest.setAmount(amount);
		paymentRequest.setMerchant_id(merchantId);
		paymentRequest.setOrder_number(orderId);
		paymentRequest.getCard().setName("John Doe")
				.setNumber("5100000010001004").setExpiry_month("12")
				.setExpiry_year("18").setCvd("123");
		return paymentRequest;
	}

	@Test(expected = BeanstreamApiException.class)
	public void invalidPaymentIdVoidPayment() throws BeanstreamApiException {
		beanstream.payments().voidPayment("-1", 90.00d);
	}

	@Test(expected = BeanstreamApiException.class)
	public void emptyPaymentIdVoidPayment() throws BeanstreamApiException {
		beanstream.payments().voidPayment("", 90.00d);
	}

	@Test(expected = BeanstreamApiException.class)
	public void invalidAmountVoidPayment() throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("PEDRO"), "300200578", "90.00");

		PaymentResponse response = null;
		try {
			response = beanstream.payments().makePayment(paymentRequest);
		} catch (BeanstreamApiException ex) {
			// not testing makePayment operation, so ignore
		}
		
		if (response != null && response.isApproved()) {
			response = beanstream.payments().voidPayment(response.id, 0.00);
			Assert.fail("invalid transaction amount expected (BeanstreamApiException)");
		} else {
			Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
		}
	}
}
