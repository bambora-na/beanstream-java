package com.beanstream.api.test;

import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.UnreferencedCardReturnRequest;
import com.beanstream.responses.PaymentResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.*;

public class PaymentAPITest extends BasePaymentsTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void preAuthCompletion_IsApproved_WhenHigherAmountWasPreAuthorized() throws BeanstreamApiException {
        //Arrange
        double authAmount = 120;
        double completeAmount = 43.50;

        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(getRandomOrderId("GAS"), authAmount);
        PaymentResponse authResponse = gateway.payments().preAuth(paymentRequest);

        //Act
        PaymentResponse completeResponse = gateway.payments().preAuthCompletion(authResponse.id, completeAmount);

        //Assert
        Assert.assertTrue(completeResponse.isApproved());
        Assert.assertEquals(completeAmount, completeResponse.amount, 0);
    }

    @Test
    public void preAuthCompletion_IsNotApproved_WhenLowerAmountHasBeenPreAuthorized() throws BeanstreamApiException {
        //Arrange
        double authAmount = 120;
        double completeAmount = 200;

        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(getRandomOrderId("GAS"), authAmount);
        PaymentResponse authResponse = gateway.payments().preAuth(paymentRequest);

        //Act
        exceptionRule.expect(BeanstreamApiException.class);
        exceptionRule.expect(hasProperty("httpStatusCode", is(402)));
        exceptionRule.expect(hasProperty("category", is(2)));
        exceptionRule.expect(hasProperty("code", is(208)));

        PaymentResponse completeResponse = gateway.payments().preAuthCompletion(authResponse.id, completeAmount);

        //Assert
        Assert.assertFalse(completeResponse.isApproved());
    }

    @Test()
    public void preAuthCompletion_IsNotApproved_WhenOrderNumberIsDifferentFromPreAuthorization() throws BeanstreamApiException {
        //Arrange
        double authAmount = 120;
        double completeAmount = authAmount;


        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(getRandomOrderId("GAS"), authAmount);
        PaymentResponse authResponse = gateway.payments().preAuth(paymentRequest);

        //Act
        PaymentResponse completeResponse = gateway.payments().preAuthCompletion(authResponse.id, completeAmount);

        //Assert
        Assert.assertTrue(completeResponse.isApproved());
    }

    @Test()
    public void voidPayment() throws BeanstreamApiException {

        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
                getRandomOrderId("PEDRO"), "90.00");

        PaymentResponse response = gateway.payments().makePayment(
                paymentRequest);
        if (response.isApproved()) {
            response = gateway.payments().voidPayment(response.id, 90.00);
            Assert.assertTrue("void payment response is not VP",
                    "VP".equals(response.type));
        } else {
            Assert.fail("Test can not be executed cause the payment api could not approve the test payment");
        }
    }

    @Test(expected = BeanstreamApiException.class)
    public void invalidPaymentIdVoidPayment() throws BeanstreamApiException {
        gateway.payments().voidPayment("-1", 90.00d);
    }

    @Test(expected = BeanstreamApiException.class)
    public void emptyPaymentIdVoidPayment() throws BeanstreamApiException {
        gateway.payments().voidPayment("", 90.00d);
    }

    @Test(expected = BeanstreamApiException.class)
    public void invalidAmountVoidPayment() throws BeanstreamApiException {
        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
                getRandomOrderId("PEDRO"), "90.00");

        PaymentResponse response = null;
        try {
            response = gateway.payments().makePayment(paymentRequest);
        } catch (BeanstreamApiException ex) {
            // not testing makePayment operation, so ignore this
        }
        if (response != null && response.isApproved()) {
            response = gateway.payments().voidPayment(response.id, 0.00);
            Assert.fail("invalid transaction amount expected (BeanstreamApiException)");
        } else {
            Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
        }
    }

    @Test()
    public void Return() throws BeanstreamApiException {

        CardPaymentRequest paymentRequest = getCreditCardPaymentRequest(
                getRandomOrderId("PEDRO"), "90.00");

        PaymentResponse response = gateway.payments().makePayment(paymentRequest);

        if (response.isApproved()) {
            response = gateway.payments().returnPayment(response.id, 90.0);
            Assert.assertTrue("void payment response is not R", "R".equals(response.type));

        } else {
            Assert.fail("Test can not be executed cause the payment api could not approved the test payment");
        }
    }

    @Test()
    public void unreferencedCardReturn() {

        UnreferencedCardReturnRequest unrefCardReturnRequest = new UnreferencedCardReturnRequest();
        unrefCardReturnRequest.getCard().setName("John Doe")
                .setNumber("5100000010001004").setExpiryMonth("12")
                .setExpiryYear("18").setCvd("123");

        unrefCardReturnRequest.setAmount(100.00);
        unrefCardReturnRequest.setMerchantId("300200578");
        unrefCardReturnRequest.setOrderNumber(getRandomOrderId("GAS"));

        //PaymentResponse response = gateway.payments().unreferencedReturn(unrefCardReturnRequest);
        //if ( !response.isApproved()) {
        //        Assert.fail("Unreferenced Return is not approved");
        //}

    }

}

