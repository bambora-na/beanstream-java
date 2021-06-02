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

import static com.beanstream.connection.BeanstreamUrls.getPaymentUrl;
import static com.beanstream.connection.BeanstreamUrls.getPreAuthCompletionsUrl;
import static com.beanstream.connection.BeanstreamUrls.getReturnUrl;
import static com.beanstream.connection.BeanstreamUrls.getUnreferencedReturnUrl;
import static com.beanstream.connection.BeanstreamUrls.getVoidPaymentUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.HttpStatus;

import com.beanstream.Configuration;
import com.beanstream.Gateway;
import com.beanstream.connection.BeanstreamUrls;
import com.beanstream.connection.HttpMethod;
import com.beanstream.connection.HttpsConnector;
import com.beanstream.exceptions.BeanstreamApiException;
import com.beanstream.requests.CardPaymentRequest;
import com.beanstream.requests.CashPaymentRequest;
import com.beanstream.requests.ChequePaymentRequest;
import com.beanstream.requests.InteracPaymentRequest;
import com.beanstream.requests.PaymentRequest;
import com.beanstream.requests.ProfilePaymentRequest;
import com.beanstream.requests.ReturnRequest;
import com.beanstream.requests.TokenPaymentRequest;
import com.beanstream.requests.UnreferencedCardReturnRequest;
import com.beanstream.requests.UnreferencedSwipeReturnRequest;
import com.beanstream.responses.BeanstreamResponse;
import com.beanstream.responses.InteracPaymentResponse;
import com.beanstream.responses.PaymentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * The entry point for processing payments.
 *
 * @author bowens
 */
public class PaymentsAPI {

    private static final String AMOUNT_PARAM = "amount";
    private static final String MERCHANT_ID_PARAM = "merchant_id";
    private static ThreadLocal<StringBuffer> communicationLog = new ThreadLocal<>();
    private Configuration config;
    private HttpsConnector connector;
    private final Gson gson = new Gson();

    public PaymentsAPI(Configuration config) {
        this.config = config;
        connector = new HttpsConnector(config.getMerchantId(),
                config.getPaymentsApiPasscode());
        connector.setCustomHttpClient(config.getCustomHttpClient());
    }

    public static String getLog() {
    	StringBuffer sb = communicationLog.get();
    	if (sb!=null) {
    		try {
    			return sb.toString();
    		} finally {
    			clearLog();
    		}
    	}
    	return null;
    }

    private static StringBuffer getLogInternal() {
    	StringBuffer sb = communicationLog.get();
    	if (sb==null) {
    		sb = new StringBuffer();
    		communicationLog.set( sb );
    	}
    	return sb;
    }

    public static void addLog(String str) {
    	if (str!=null && str.length()>0) {
    		StringBuffer sb = getLogInternal();
    		if (sb.length()>0)
    			sb.append( "\n\n" );
    		sb.append( str );
    	}
    }
    
    public static void clearLog() {
    	communicationLog.remove();
    }
    
    public void setConfig(Configuration config) {
        this.config = config;
        connector = new HttpsConnector(config.getMerchantId(),
                config.getPaymentsApiPasscode());
        connector.setCustomHttpClient(config.getCustomHttpClient());
    }

    /**
     * Make a credit card payment. This payment must include credit card data.
     * An Approved request will return a PaymentResponse. If the request fails in
     * any way, even a card Decline, then an exception will be thrown.
     *
     * @author Chris Tihor
     * @param paymentRequest the payment request including a credit card data
     * @return PaymentResponse the result of the payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public PaymentResponse makePayment(CardPaymentRequest paymentRequest)
            throws BeanstreamApiException {
    	clearLog();

    	paymentRequest.setMerchantId("" + config.getMerchantId());
        paymentRequest.getCard().setComplete(true); // false for pre-auth

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl(config.getPlatform(), config.getVersion());
        
        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url,
                paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);
    }

    /**
     * Make a tokenized payment. This payment must include a token that was previously
     * returned from the Legato tokenizing service, usually by the client application.
     *
     * @author Chris Tihor
     * @param paymentRequest the payment request including a token
     * @return PaymentResponse the result of the payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error
     */
    public PaymentResponse makePayment(TokenPaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

    	paymentRequest.setMerchantId("" + config.getMerchantId());
        paymentRequest.getToken().setComplete(true); // true to make the payment

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl( config.getPlatform(), config.getVersion());

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * Make a tokenized payment with a Payment Profile. This payment must include a token that was previously
     * returned from the Payment Profile.
     *
     * @param paymentRequest the payment request including a token
     * @return PaymentResponse the result of the payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error
     */
    public PaymentResponse makePayment(ProfilePaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

    	paymentRequest.setMerchantId("" + config.getMerchantId());
        paymentRequest.getProfile().setComplete(true); // true to make the payment

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl( config.getPlatform(), config.getVersion());

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);
    }

    /**
     * Make an interac payment request.
     *
     * @author ilya
     * @param paymentRequest the interac payment request
     * @return InteracPaymentResponse the result of the interac payment request
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public InteracPaymentResponse interacPayment(InteracPaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl( config.getPlatform(), config.getVersion());

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a InteracPaymentResponse
        InteracPaymentResponse res = gson.fromJson(response, InteracPaymentResponse.class);
        try {
        	// contents is URL-encoded for some reason
        	res.contents = URLDecoder.decode( res.contents, "UTF-8" );
        } catch (UnsupportedEncodingException e) {}
        return res;
    }
    
    /**
     * Complete the interac payment.
     *
     * @author ilya
     * @param merchantData - this value should be stored in session while client is redirected to financial institution website
     * @param paymentRequest the interac payment request should have InteracResponse object populated (important!)
     * @return PaymentResponse the result of the interac payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public PaymentResponse interacPaymentCompletion(String merchantData, InteracPaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

        // build the URL
        String url = BeanstreamUrls.getPaymentContinuationsUrl( config.getPlatform(), config.getVersion(), merchantData);

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * Make a cash payment.
     *
     * @author Chris Tihor
     * @param paymentRequest the cash payment request
     * @return PaymentResponse the result of the payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public PaymentResponse makePayment(CashPaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl( config.getPlatform(), config.getVersion());

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * Make a payment by cheque.
     *
     * @author Chris Tihor
     * @param paymentRequest the payment request including a cheque
     * @return PaymentResponse the result of the payment transaction
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public PaymentResponse makePayment(ChequePaymentRequest paymentRequest) throws BeanstreamApiException {
    	clearLog();

        // build the URL
        String url = BeanstreamUrls.getPaymentUrl( config.getPlatform(), config.getVersion());

        addLog(url);
        addLog(gson.toJson( paymentRequest ));

        // process the transaction using the REST API
        String response = connector.ProcessTransaction(HttpMethod.post, url, paymentRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
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
     * @param paymentId payment transaction id to void
     * @param amount the amount to avoid in this transaction
     * @return PaymentResponse as result you will received a payment response
     * with the same payment transaction id but with the type 'VP'
     * @throws BeanstreamApiException as a result of a business logic validation
     * or any other error @see
     */
    public PaymentResponse voidPayment(String paymentId, double amount)
            throws BeanstreamApiException {
    	clearLog();

    	Gateway.assertNotEmpty(paymentId, "invalid paymentId");
        String url = getVoidPaymentUrl(config.getPlatform(),
                config.getVersion(), paymentId);

        JsonObject voidRequest = new JsonObject();
        voidRequest.addProperty(MERCHANT_ID_PARAM,
                String.valueOf(config.getMerchantId()));
        voidRequest.addProperty(AMOUNT_PARAM, String.valueOf(amount));

        addLog(url);
        addLog(gson.toJson( voidRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post, url,
                voidRequest);

        addLog(response);
        
        // parse the output and return a PaymentResponse
        return gson.fromJson(response, PaymentResponse.class);

    }

    /**
     * <p>
     * Pre-authorize a payment. Use this if you want to know if a customer has
     * sufficient funds before processing a payment. A real-world example of
     * this is pre-authorizing at the gas pump for $100 before you fill up, then
     * end up only using $60 of gas; the customer is only charged $60. The final
     * payment is used with preAuthCompletion() method.
     * </p>
     *
     * @param paymentRequest payment request to pre authorize with a valid
     * amount
     * @return a PaymentResponse pre-approved containing the paymentId you will
     * need to complete the transaction.
     * @throws BeanstreamApiException if any validation fail or error occur
     */
    public PaymentResponse preAuth(CardPaymentRequest paymentRequest)
            throws BeanstreamApiException {
    	clearLog();

        if (paymentRequest == null || paymentRequest.getCard() == null) {
            // TODO - do we need to supply category and code ids here?
            BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid payment request");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
        }

        paymentRequest.getCard().setComplete(false);

        String preAuthUrl = getPaymentUrl(config.getPlatform(),
                config.getVersion());

        addLog(preAuthUrl);
        addLog(gson.toJson( paymentRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post,
                preAuthUrl, paymentRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * <p>
     * Pre-authorize a payment. Use this if you want to know if a customer has
     * sufficient funds before processing a payment. A real-world example of
     * this is pre-authorizing at the gas pump for $100 before you fill up, then
     * end up only using $60 of gas; the customer is only charged $60. The final
     * payment is used with preAuthCompletion() method.
     * </p>
     *
     * @param paymentRequest payment request to pre authorize with a valid
     * amount
     * @return a PaymentResponse pre-approved containing the paymentId you will
     * need to complete the transaction.
     * @throws BeanstreamApiException if any validation fail or error occur
     */
    public PaymentResponse preAuth(ProfilePaymentRequest paymentRequest)
            throws BeanstreamApiException {
    	clearLog();

        if (paymentRequest == null || paymentRequest.getProfile() == null) {
            // TODO - do we need to supply category and code ids here?
            BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid payment request");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
        }

        paymentRequest.getProfile().setComplete(false);

        String preAuthUrl = getPaymentUrl(config.getPlatform(),
                config.getVersion());

        addLog(preAuthUrl);
        addLog(gson.toJson( paymentRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post,
                preAuthUrl, paymentRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * <p>
     * Pre-authorize a token payment. Use this if you want to know if a customer has
     * sufficient funds before processing a payment. A real-world example of
     * this is pre-authorizing at the gas pump for $100 before you fill up, then
     * end up only using $60 of gas; the customer is only charged $60. The final
     * payment is used with preAuthCompletion() method.
     * </p>
     *
     * @param paymentRequest payment request to pre authorize with a valid
     * amount
     * @return a PaymentResponse pre-approved containing the paymentId you will
     * need to complete the transaction.
     * @throws BeanstreamApiException if any validation fail or error occur
     */
    public PaymentResponse preAuth(TokenPaymentRequest paymentRequest)
            throws BeanstreamApiException {
    	clearLog();

        if (paymentRequest == null || paymentRequest.getToken() == null) {
            BeanstreamResponse response = BeanstreamResponse.fromMessage("invalid payment request");
            throw BeanstreamApiException.getMappedException(HttpStatus.SC_BAD_REQUEST, response);
        }

        paymentRequest.getToken().setComplete(false);

        String preAuthUrl = getPaymentUrl(config.getPlatform(), config.getVersion());

        addLog(preAuthUrl);
        addLog(gson.toJson( paymentRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post, preAuthUrl, paymentRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);
    }

    /**
     * Push the actual payment through after a pre-authorization.
     * Convenience method if you don't want to supply the whole PaymentRequest.
     * 
     * @param paymentId of the pre-authorized transaction
     * @param amount final amount to be charged
     * @return the PaymentResponse for the final transaction
     * @throws BeanstreamApiException
     */
    public PaymentResponse preAuthCompletion(String paymentId, double amount) throws BeanstreamApiException {
    	clearLog();

    	Gateway.assertNotEmpty(paymentId, "Invalid Payment Id");

        String authorizePaymentUrl = getPreAuthCompletionsUrl(
                config.getPlatform(), config.getVersion(), paymentId);

        JsonObject authorizeRequest = new JsonObject();
        authorizeRequest.addProperty(MERCHANT_ID_PARAM,
                String.valueOf(config.getMerchantId()));
        authorizeRequest.addProperty(AMOUNT_PARAM, String.valueOf(amount));
        
        addLog(authorizePaymentUrl);
        addLog(gson.toJson( authorizeRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post,
                authorizePaymentUrl, authorizeRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);

    }

    /**
     * Push the actual payment through after a pre-authorization.
     * You can supply the PaymentRequest to set any fields you did not set in the pre-auth.
     * @param paymentId to complete
     * @param request that will be saved
     * @return PaymentResponse
     * @throws BeanstreamApiException if the transaction was declined 
     */
    public PaymentResponse preAuthCompletion(String paymentId, PaymentRequest request) throws BeanstreamApiException {
    	clearLog();

    	Gateway.assertNotEmpty(paymentId, "Invalid Payment Id");
        String authorizePaymentUrl = getPreAuthCompletionsUrl(
                config.getPlatform(), config.getVersion(), paymentId);
        
        addLog(authorizePaymentUrl);
        addLog(gson.toJson( request ));

        String response = connector.ProcessTransaction(HttpMethod.post, authorizePaymentUrl, request);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);
    }
    
    /**
     * Return a previous payment made through Beanstream.
     *
     * @param paymentId payment transaction id to return
     * @param amount final amount to be returned
     * @return the PaymentResponse for the final transaction
     * @throws BeanstreamApiException
     */
    public PaymentResponse returnPayment(String paymentId, double amount) throws BeanstreamApiException {
    	clearLog();

    	Gateway.assertNotEmpty(paymentId, "Invalid Payment Id");

        String returnPaymentUrl = getReturnUrl(config.getPlatform(), config.getVersion(), paymentId);

        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setMerchantId( String.valueOf(config.getMerchantId()) );
        returnRequest.setAmount( amount );
        
        addLog(returnPaymentUrl);
        addLog(gson.toJson( returnRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post, returnPaymentUrl, returnRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);

    }

    /**
     * Return a previous card payment that was not made through Beanstream. Use
     * this if you would like to return a payment but that payment was performed
     * on another gateway.
     *
     * @param returnRequest of the UnreferencedCardReturnRequest
     * @return the PaymentResponse for the final transaction
     * @throws BeanstreamApiException
     */
    public PaymentResponse unreferencedReturn(UnreferencedCardReturnRequest returnRequest) throws BeanstreamApiException {
    	clearLog();

        String unreferencedReturnUrl = getUnreferencedReturnUrl(
                config.getPlatform(), config.getVersion());

        returnRequest.setMerchantId( String.valueOf(config.getMerchantId()) );

        addLog(unreferencedReturnUrl);
        addLog(gson.toJson( returnRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post, unreferencedReturnUrl, returnRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);

    }

    /**
     * Return a previous swipe payment that was not made through Beanstream. Use
     * this if you would like to return a payment but that payment was performed
     * on another payment service.
     *
     * @param returnRequest of the UnreferencedSwipeReturnRequest
     * @return the PaymentResponse for the final transaction
     * @throws BeanstreamApiException
     */
    public PaymentResponse unreferencedReturn(UnreferencedSwipeReturnRequest returnRequest) throws BeanstreamApiException {
    	clearLog();

        String unreferencedReturnUrl = getUnreferencedReturnUrl(
                config.getPlatform(), config.getVersion());

        returnRequest.setMerchantId( String.valueOf(config.getMerchantId()) );

        addLog(unreferencedReturnUrl);
        addLog(gson.toJson( returnRequest ));

        String response = connector.ProcessTransaction(HttpMethod.post, unreferencedReturnUrl, returnRequest);

        addLog(response);
        
        return gson.fromJson(response, PaymentResponse.class);

    }

}
