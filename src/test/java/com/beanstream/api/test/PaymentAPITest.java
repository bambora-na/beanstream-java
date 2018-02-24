package com.beanstream.api.test;

import junit.framework.Assert;

import org.junit.Test;

import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.UnreferencedCardReturnRequest;
import com.beanstream.responses.PaymentResponse;

public class PaymentAPITest extends BaseBeanstreamTest{

	@Test
	public void preAuth() throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);

		if (response.isApproved()) {
			PaymentResponse authResp = beanstream.payments().preAuthCompletion(
					response.id, 43.50);
			if (!authResp.isApproved()) {
				Assert.fail("This auth completion should be approved because a greater amount has been pre authorized");
			}
		}
	}

	@Test
	public void preAuthCompletionGreaterAmount() throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);
		try {
			if (response.isApproved()) {
				PaymentResponse authResp = beanstream.payments()
						.preAuthCompletion(response.id, 200);
				if (authResp.isApproved()) {
					Assert.fail("This auth completion should be not be approved because a lower amount has been pre authorized");
				}
			}
		} catch (BeanstreamApiException ex) {
                    Assert.assertEquals("Http status code did not match expected.", 402, ex.getHttpStatusCode());
                    Assert.assertEquals("Completion greater than remaining reserve amount., details: ", 2, ex.getCategory());
                    Assert.assertEquals("Error code did not match expected", 208, ex.getCode());
		}
	}

	@Test()
	public void preAuthCompletionDifferentOrderNumber()
			throws BeanstreamApiException {
		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("GAS"), "120.00");
		PaymentResponse response = beanstream.payments()
				.preAuth(paymentRequest);

		if (response.isApproved()) {
			PaymentResponse authResp = beanstream.payments().preAuthCompletion(response.id, 120.00);
			if (!authResp.isApproved()) {
				Assert.fail("This auth completion should be not be approved because the order number is diffrent than the pre-authorized one");
			}

		}
	}

	@Test()
	public void voidPayment() throws BeanstreamApiException {

		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("PEDRO"),  "90.00");

		PaymentResponse response = beanstream.payments().makePayment(
				paymentRequest);
		if (response.isApproved()) {
			response = beanstream.payments().voidPayment(response.id, 90.00);
			Assert.assertTrue("void payment response is not VP",
					"VP".equals(response.type));
		} else {
			Assert.fail("Test can not be executed cause the payment api could not approve the test payment");
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
				getRandomOrderId("PEDRO"),  "90.00");

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
        
    @Test()
	public void Return() throws BeanstreamApiException {

		CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
				getRandomOrderId("PEDRO"),  "90.00");

		PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
                
		if (response.isApproved()) {
			response = beanstream.payments().returnPayment(response.id, 90.0);
			Assert.assertTrue("void payment response is not R", "R".equals(response.type));
                        
		} else {
			Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
		}
	}

    @Test()
	public void unreferencedCardReturn() throws BeanstreamApiException {

            UnreferencedCardReturnRequest unrefCardReturnRequest = new UnreferencedCardReturnRequest();
            unrefCardReturnRequest.getCard().setName("John Doe")
                            .setNumber("5100000010001004").setExpiryMonth("12")
                            .setExpiryYear("18").setCvd("123");

            unrefCardReturnRequest.setAmount( 100.00 );
            unrefCardReturnRequest.setMerchantId( "300200578" );
            unrefCardReturnRequest.setOrderNumber( getRandomOrderId("GAS") );

            //PaymentResponse response = beanstream.payments().unreferencedReturn(unrefCardReturnRequest);
            //if ( !response.isApproved()) {
            //        Assert.fail("Unreferenced Return is not approved");
            //}

        }
        
}

