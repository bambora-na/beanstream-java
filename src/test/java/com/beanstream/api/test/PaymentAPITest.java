package com.beanstream.api.test;

import com.beanstream.Gateway;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.responses.BeanstreamResponse;
import com.beanstream.responses.BeanstreamResponseBuilder;
import com.beanstream.responses.PaymentResponse;
import junit.framework.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentAPITest {

	private AtomicInteger sequence = new AtomicInteger(1);
	Gateway beanstream = new Gateway("v1", 300200578,
			"4BaD82D9197b4cc4b70a221911eE9f70");

	@Test
	public void preAuth() throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "300200578", "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);

		if (response.isApproved()) {
			PaymentResponse authResp = beanstream.payments().preAuthCompletion(
					response.id, 43.50, null);
			if (!authResp.isApproved()) {
				Assert.fail("This auth completion should be approved because a greater amount has been pre authorized");
			}
		}
	}

	@Test
	public void preAuthCompletionGreaterAmount() throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "300200578", "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);
		try {
			if (response.isApproved()) {
				PaymentResponse authResp = beanstream.payments()
						.preAuthCompletion(response.id, 200,
								response.order_number);
				if (authResp.isApproved()) {
					Assert.fail("This auth completion should be not be approved because a lower amount has been pre authorized");
				}
			}
		} catch (BeanstreamApiException ex) {
            BeanstreamResponse gatewayResp = new BeanstreamResponseBuilder()
                    .withCode(208)
                    .withCategory(2)
                    .withMessage("Completion greater than remaining reserve amount.")
                    .withHttpStatusCode(400)
                    .build();
            Assert.assertEquals(
					"This auth completion should be not be approved because a lower amount has been pre authorized",
					ex.getHttpStatusCode(), 400);
			Assert.assertEquals(
					"This auth completion should be not be approved because a lower amount has been pre authorized",
					BeanstreamResponse.fromException(ex),
					gatewayResp);
		}
	}

	@Test()
	public void preAuthCompletionDifferentOrderNumber()
			throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "300200578", "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);

		if (response.isApproved()) {
			PaymentResponse authResp = beanstream.payments().preAuthCompletion(
					response.id, 120.00, response.order_number + 1);
			if (!authResp.isApproved()) {
				Assert.fail("This auth completion should be not be approved because the order number is diffrent than the pre-authorized one");
			}

		}
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
			// not testing makePayment operation, so ignore this
		}
		if (response != null && response.isApproved()) {
			response = beanstream.payments().voidPayment(response.id, 0.00);
			Assert.fail("invalid transaction amount expected (BeanstreamApiException)");
		} else {
			Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
		}
	}

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
}
