package com.beanstream.api.test;

import com.beanstream.Gateway;
import com.beanstream.connection.HttpMethod;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.domain.Address;
import com.beanstream.domain.Card;
import com.beanstream.domain.PaymentProfile;
import com.beanstream.domain.Token;
import com.beanstream.domain.Transaction;
import com.beanstream.domain.TransactionRecord;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.CashPaymentRequest;
import com.beanstream.requests.ChequePaymentRequest;
import com.beanstream.requests.Criteria;
import com.beanstream.requests.LegatoTokenRequest;
import com.beanstream.requests.Operators;
import com.beanstream.requests.ProfilePaymentRequest;
import com.beanstream.requests.ProfilePaymentRequestData;
import com.beanstream.requests.QueryFields;
import com.beanstream.requests.TokenPaymentRequest;
import com.beanstream.responses.BeanstreamResponse;
import com.beanstream.responses.LegatoTokenResponse;
import com.beanstream.responses.PaymentResponse;
import com.beanstream.responses.ProfileResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

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
        /*t.testPayment();
        t.testTokenPayment();
        t.testVoidPayment();
        t.testPreAuthorization();
        t.testGetTransaction();
        t.testQueryTransactions();
        t.testProfileCrud();
        t.testProfileCrudUsingToken();*/
    }

    private final AtomicInteger sequence = new AtomicInteger(1);

    private String getRandomOrderId(String prefix) {
        String orderId;
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

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testPayment() {

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        /* Test Card Payment */
        CardPaymentRequest req = new CardPaymentRequest();
        req.setAmount(100.00)
                .setOrderNumber(getRandomOrderId("test"));
        req.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {

            PaymentResponse response = beanstream.payments().makePayment(req);
            System.out.println("Card Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }

        /* Test Cash Payment */
        CashPaymentRequest cashReq = new CashPaymentRequest();
        cashReq.setAmount(123.45);
        cashReq.setOrderNumber(getRandomOrderId("cash"));

        try {

            PaymentResponse response = beanstream.payments().makePayment(cashReq);
            System.out.println("Cash Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }

        /* Test Cheque Payment */
        ChequePaymentRequest chequeReq = new ChequePaymentRequest();
        chequeReq.setAmount(668.99);
        chequeReq.setOrderNumber(getRandomOrderId("cheque"));

        try {

            PaymentResponse response = beanstream.payments().makePayment(chequeReq);
            System.out.println("Cheque Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testTokenPayment() {

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");
        HttpsConnector connector = new HttpsConnector(300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        /* Test Token Payment */
        // The first step is to call the Legato service to get a token.
        // This is normally performed on the client machine, and not on the
        // server.
        // The goal with tokens is to not have credit card information move
        // through your server,
        // thus lowering your scope for PCI compliance
        LegatoTokenRequest legatoTokenRequest = new LegatoTokenRequest();
        legatoTokenRequest.number = "5100000010001004";
        legatoTokenRequest.expiryMonth = 12;
        legatoTokenRequest.expiryYear = 18;
        legatoTokenRequest.cvd = "123";

        String url = "https://www.beanstream.com/scripts/tokenization/tokens";
        String output = "";
        try {
            output = connector.ProcessTransaction(HttpMethod.post, url, legatoTokenRequest);
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(SampleTransactions.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.getMessage());
        }

        // Parse the output and return a token response to get the token for the
        // payment request
        Gson gson = new Gson();
        LegatoTokenResponse tokenResponse = gson.fromJson(output, LegatoTokenResponse.class);

        System.out.println("token: " + output);

        TokenPaymentRequest tokenReq = new TokenPaymentRequest();
        tokenReq.setAmount(100.00);
        tokenReq.setOrderNumber(getRandomOrderId("token"));
        tokenReq.getToken()
                .setName("John Doe")
                .setCode(tokenResponse.getToken());

        try {
            PaymentResponse response = beanstream.payments().makePayment(tokenReq);
            System.out.println("Token Payment Approved? " + response.isApproved());

        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }

        // Pre-Auth and Complete

        try {
            output = connector.ProcessTransaction(HttpMethod.post, url, legatoTokenRequest);
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(SampleTransactions.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.getMessage());
        }
        tokenResponse = gson.fromJson(output, LegatoTokenResponse.class);

        System.out.println("Token pre-auth: " + tokenResponse.getToken());

        TokenPaymentRequest req = new TokenPaymentRequest();
        req.setAmount(80.00);
        req.setOrderNumber(getRandomOrderId("token"));
        req.getToken()
                .setName("John Doe")
                .setCode(tokenResponse.getToken());

        try {
            PaymentResponse response = beanstream.payments().preAuth(req);
            System.out.println("Token Payment Approved? " + response.isApproved());
            response = beanstream.payments().preAuthCompletion(response.id, 55.30);
            Assert.assertTrue(response.isApproved());
            Assert.assertEquals("PAC", response.type);
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testVoidPayment() {

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setAmount(90.00);
        paymentRequest.setOrderNumber(getRandomOrderId("PEDRO"));
        paymentRequest.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {
            PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
            if (response.isApproved()) {
                Gson gsonpp = new GsonBuilder().setPrettyPrinting().create();
                System.out.println("Your Payment has been approved response: \n" + gsonpp.toJson(response));

                response = beanstream.payments().voidPayment(response.id, 90.00);

                if ("VP".equals(response.type)) {
                    System.out.println("The payment was voided");

                } else {
                    System.out.println("The payment was not voided");
                }
            }
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }

    }

    @Test
    public void testReturnPayment() {

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setAmount(70.00);
        paymentRequest.setOrderNumber(getRandomOrderId("PEDRO"));
        paymentRequest.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {
            PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
            if (response.isApproved()) {
                Gson gsonpp = new GsonBuilder().setPrettyPrinting().create();
                System.out.println("Your Payment has been approved response: \n" + gsonpp.toJson(response));

                response = beanstream.payments().returnPayment(response.id, 70.00);

                if ("R".equals(response.type)) {
                    System.out.println("The payment was retuned");

                } else {
                    System.out.println("The payment was not returned");
                }
            }
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }

    }

    @Test
    public void testPreAuthorization() {

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setAmount(90.00);
        paymentRequest.setOrderNumber(getRandomOrderId("GAS"));
        paymentRequest.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {
            PaymentResponse response = beanstream.payments().preAuth(paymentRequest);
            PaymentResponse authResp = beanstream.payments().preAuthCompletion(response.id, 43.50);
            if (!authResp.isApproved()) {
                Assert.fail("This auth completion should be approved because a greater amount has been pre authorized");
            }
        } catch (BeanstreamApiException ex) {
            System.out.println(BeanstreamResponse.fromException(ex));
            Assert.fail(ex.getMessage());
        }

        // Pre-auth and complete again but supply PaymentRequest details in the complete
        CardPaymentRequest paymentRequest2 = new CardPaymentRequest();
        paymentRequest2.setAmount(30.00);
        paymentRequest2.setOrderNumber(getRandomOrderId("Pumpkins"));
        paymentRequest2.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {
            PaymentResponse response = beanstream.payments().preAuth(paymentRequest2);
            paymentRequest2.setAmount(4.00);
            PaymentResponse authResp = beanstream.payments().preAuthCompletion(response.id, paymentRequest2);
            if (!authResp.isApproved()) {
                Assert.fail("This auth completion should be approved because a greater amount has been pre authorized");
            }

        } catch (BeanstreamApiException ex) {
            System.out.println(BeanstreamResponse.fromException(ex));
            Assert.fail(ex.getMessage());
        }
    }

    @Ignore("Test is timing out currently, disabling for now")
    @Test
    public void testGetTransaction() {
        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode
        
        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setAmount(30.00)
                .setOrderNumber(getRandomOrderId("get"));
        paymentRequest.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");
            
        try {
            PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
            Assert.assertTrue(response.isApproved());
            
            if (response.isApproved()) {
                Transaction transaction = beanstream.reports().getTransaction(response.id);
                System.out.println("Transaction: " + transaction.getAmount() + " approved? " + transaction.getApproved());
            }
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred", ex);
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testQueryTransactions() {
        System.out.println("############################################################3");
        System.out.println("############################################################3");
        System.out.println("############################################################3");
        System.out.println("############################################################3");
        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

        String order = getRandomOrderId("q");
        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setAmount(20.50)
                .setOrderNumber(order);
        paymentRequest.getCard().setName("Bob Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        try {
            PaymentResponse response = beanstream.payments().makePayment(paymentRequest);
            if (response.isApproved()) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -1);
                Date startDate = cal.getTime(); // yesterday
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                Date endDate = cal.getTime(); // tomorrow
                Criteria[] searchFilter = new Criteria[]{
                        new Criteria(QueryFields.OrderNumber, Operators.Equals, order)
                };
                List<TransactionRecord> query = beanstream.reports().query(startDate, endDate, 1, 2, searchFilter);
                Assert.assertNotNull(query);
                Assert.assertFalse(query.isEmpty());

                System.out.println("Queried " + query.size() + " items.");

                // print out the first 10 records
                int i = 0;
                for (TransactionRecord tr : query) {
                    System.out.println(tr.getTransactionId() + " from " + tr.getCardOwner() + " -> $" + tr.getAmount());
                    i++;
                    if (i > 10) {
                        break;
                    }
                }

            }
        } catch (BeanstreamApiException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "An error occurred: " + ex.toString(), ex);
            System.out.println("Error Details: " + ex.getCode() + ", " + ex.getCategory());
            Assert.fail(ex.getMessage());
        }

    }

    @Test
    public void testProfileCrud() {
        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

        String profileId = null;

        try {
            Address billing = getTestCardValidAddress();
            Card card = new Card().setName("John Doe")
                    .setNumber("5100000010001004")
                    .setExpiryMonth("12")
                    .setExpiryYear("18")
                    .setCvd("123");

            // test create profile
            ProfileResponse createdProfile = beanstream.profiles().createProfile(card, billing);
            profileId = createdProfile.getId();
            System.out.println(createdProfile);

            // test get profile by id
            PaymentProfile paymentProfile = beanstream.profiles().getProfileById(profileId);
            System.out.println(paymentProfile);
            Assert.assertNotNull(paymentProfile);

            // update the profile to francais
            paymentProfile.setLanguage("fr");
            paymentProfile.setComments("test updating profile sending billing info only");
            // update profile
            beanstream.profiles().updateProfile(paymentProfile);

            // refresh the updated profile
            paymentProfile = beanstream.profiles().getProfileById(profileId);
            System.out.println(paymentProfile);
            Assert.assertNotNull(paymentProfile);
            Assert.assertEquals("fr", paymentProfile.getLanguage());

            // add a new card
            Card newCard = new Card()
                    .setCvd("123")
                    .setName("Tester Doe")
                    .setNumber("4030000010001234")
                    .setExpiryMonth("01")
                    .setExpiryYear("19");
            ProfileResponse newCardResp = beanstream.profiles().addCard(profileId, newCard);
            System.out.println(newCardResp);

            // get all cards
            List<Card> profileCards = beanstream.profiles().getCards(profileId);
            Assert.assertNotNull(profileCards);
            Assert.assertEquals("Number of cards not expected.", 2, profileCards.size());

            // update card
            Card cardUpdated = beanstream.profiles().getCard(profileId, "1");
            cardUpdated.setExpiryMonth("04");
            beanstream.profiles().updateCard(profileId, cardUpdated);

            // delete card
            ProfileResponse result = beanstream.profiles().removeCard(profileId, "2");

            // delete the payment profile
            beanstream.profiles().deleteProfileById(profileId);
            boolean notFound = false;
            try {
                beanstream.profiles().getProfileById(profileId);
                System.out.println("This profile was deleted, therefore should throw an exception");
            } catch (BeanstreamApiException e) {
                profileId = null;
                notFound = true;
            }
            Assert.assertTrue("Profile should not have been found", notFound);

        } catch (Exception ex) {
            System.out.println("unexpected exception occurred, test can not continue : " + ex.getMessage());
            Assert.fail(ex.getMessage());
        } finally {
            if (profileId != null) {
                try {
                    beanstream.profiles().deleteProfileById(profileId);
                } catch (BeanstreamApiException e) {
                    e.printStackTrace();
                    Assert.fail(e.getMessage());
                }
            }
        }

    }

    @Test
    public void testProfileCrudUsingToken() {
        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode
        HttpsConnector connector = new HttpsConnector(300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70");

        String profileId = null;
        try {

            Address billing = getTestCardValidAddress();
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
                Assert.fail(ex.getMessage());
            }

            System.out.println(output);
            // Parse the output and return a token response to get the token for the payment request
            Gson gson = new Gson();
            LegatoTokenResponse tokenResponse = gson.fromJson(output, LegatoTokenResponse.class);

            // test create profile
            Token token = new Token("John Doe", tokenResponse.getToken());

            ProfileResponse createdProfile = beanstream.profiles().createProfile(token, billing);
            profileId = createdProfile.getId();
            System.out.println(profileId);
            Assert.assertNotNull(
                    "Test failed because it should create the profile and return a valid id",
                    profileId);

            // test get profile by id
            PaymentProfile paymentProfile = beanstream.profiles().getProfileById(profileId);
            Assert.assertEquals(
                    "billing address assinged does not matches with the one sent at creation time",
                    paymentProfile.getBilling(), billing);
            Assert.assertNotNull("Credit card was not in the response", paymentProfile.getCard());
            Assert.assertTrue("The default lenguage should be english", "en".equals(paymentProfile.getLanguage()));

            // update the profile to francais
            paymentProfile.setLanguage("fr");
            paymentProfile.setComments("test updating profile sending billing info only");
            // update profile
            beanstream.profiles().updateProfile(paymentProfile);

            // refresh the updated profile
            paymentProfile = beanstream.profiles().getProfileById(profileId);

            Assert.assertEquals("Language was updated to Francais", "fr", paymentProfile.getLanguage());

            // delete the payment profile
            beanstream.profiles().deleteProfileById(profileId);
            try {
                beanstream.profiles().getProfileById(profileId);
            } catch (BeanstreamApiException e) {
                profileId = null;
            }
        } catch (BeanstreamApiException ex) {
            Assert.fail("Test can not continue, " + ex.getMessage());
        } catch (JsonSyntaxException ex) {
            Assert.fail("unexpected exception occur, test can not continue");
        } finally {
            if (profileId != null) {
                try {
                    beanstream.profiles().deleteProfileById(profileId);
                } catch (BeanstreamApiException e) {
                    Assert.fail("unexpected exception occur, test can not continue. " + e.getMessage());
                }
            }
        }

    }

    @Test
    public void testMakePaymentsWithProfile() throws BeanstreamApiException {
        //Arrange
        double authAmount = 89.0;
        double completeAmount = 66;

        Gateway gateway = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

        Address billing = getTestCardValidAddress();
        Card card = new Card().setName("JANE DOE")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        ProfileResponse profile = gateway.profiles().createProfile(card, billing);
        ProfilePaymentRequest profilePaymentRequest = new ProfilePaymentRequest();
        profilePaymentRequest
                .setProfile(new ProfilePaymentRequestData()
                        .setCardId(1)
                        .setCustomerCode(profile.getId()));
        profilePaymentRequest.setAmount(13);
        profilePaymentRequest.setOrderNumber(getRandomOrderId("TEST"));
        profilePaymentRequest.setAmount(authAmount)
                .setOrderNumber(getRandomOrderId("TEST"));

        //Act
        PaymentResponse authResponse = gateway.payments().preAuth(profilePaymentRequest);
        PaymentResponse completeResponse = gateway.payments().preAuthCompletion(authResponse.id, completeAmount);

        //Assert
        Assert.assertNotNull(authResponse);
        Assert.assertEquals("PA", authResponse.type);
        Assert.assertNotNull(completeResponse);
        Assert.assertEquals("PAC", completeResponse.type);
        Assert.assertTrue(completeResponse.isApproved());
        Assert.assertEquals(completeAmount, completeResponse.amount, 0);
    }

    @Test
    public void TestCustomConnectionParameters() {
        RequestConfig reqC = RequestConfig.custom()
                .setSocketTimeout(10) // 1/100 of a second in miliseconds
                .setConnectTimeout(10) // 1/100 of a second in miliseconds
                .build();
        HttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(reqC)
                .build();

        Gateway beanstream = new Gateway("v1", 300200578,
                "4BaD82D9197b4cc4b70a221911eE9f70", // payments API passcode
                "D97D3BE1EE964A6193D17A571D9FBC80", // profiles API passcode
                "4e6Ff318bee64EA391609de89aD4CF5d");// reporting API passcode

        beanstream.setCustomHttpsClient(client);


        // this should time out
        CardPaymentRequest req = new CardPaymentRequest();
        req.setAmount(100.00)
                .setOrderNumber(getRandomOrderId("test"));
        req.getCard()
                .setName("John Doe")
                .setNumber("5100000010001004")
                .setExpiryMonth("12")
                .setExpiryYear("18")
                .setCvd("123");

        boolean timedOut = false;
        try {

            PaymentResponse response = beanstream.payments().makePayment(req);

        } catch (BeanstreamApiException ex) {
            if ("Connection error".equalsIgnoreCase(ex.getMessage()))
                timedOut = true;
        }
    }

    private Address getTestCardValidAddress() {
        Address billing = new Address();
        billing.setName("JANE DOE");
        billing.setCity("VICTORIA");
        billing.setProvince("BC");
        billing.setCountry("CA");
        billing.setAddressLine1("123 FAKE ST.");
        billing.setPostalCode("V9T2G6");
        billing.setEmailAddress("TEST@BEANSTREAM.COM");
        billing.setPhoneNumber("12501234567");
        return billing;
    }

}
