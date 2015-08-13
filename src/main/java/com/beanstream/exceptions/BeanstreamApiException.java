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
package com.beanstream.exceptions;

/// <summary>
/// The base exception for all exceptions that can happen.
/// It holds the Http status code, the category, and the unique error code.
///
/// The Http status code is what determines which specific exception is thrown.
///
/// The 'code' is a unique error ID, an int.
///
/// The 'category' is one of 4 numeric values:
///   1	- The transaction was successfully processed
///       These can be successes or failures. They indicate that the message went to the bank and it returned a response.
///       The response could be a "payment approved" or even a "declined" response, along with many others.
///       These errors should be sent to the card holder and merchant to indicate a problem with their card.
///   2	- Business rule violation
///       This is usually a problem with how your account is configured. Sometimes it could be duplicate order number errors or something
///       similar. These errors need to be worked out by the developer before the software moves to production.
///   3	- Input data problem
///       The requests are inproperly formatted or the data is wrong. Invalid card number errors (code 52) can also appear here.
///       Mopst of these errors need to be worked out by the developer before the software moves to production.
///   4	- Transaction failed due to technical problem
///       There was a problem on the Beanstream or bank servers that is out of your control. These will respond with an http status code
///       in the 500+ range. Card holders should wait a minute and try the transaction again.
///
/// </summary>
import com.beanstream.responses.BeanstreamResponse;
import org.apache.http.HttpStatus;

/**
 *
 * @author bowens
 * @author Sanchez
 *
 */
public class BeanstreamApiException extends Exception {

    private final int code;
    private final int category;
    private final String message;
    private final int httpStatusCode;

    private static final long serialVersionUID = 1L;

    protected BeanstreamApiException(int code, int category, String message, int httpStatusCode) {
        this.code = code;
        this.category = category;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public BeanstreamApiException(Exception e, String message) {
        super(e);
        this.code = -1;
        this.category = -1;
        this.httpStatusCode = -1;
        this.message = message;
    }

    public static BeanstreamApiException getMappedException(int httpStatusCode) {
        return getMappedException(httpStatusCode, BeanstreamResponse.emptyResponse());
    }

    public static BeanstreamApiException getMappedException(int httpStatusCode, BeanstreamResponse response) {

        int code = response.getCode();
        int category = response.getCategory();
        String message = response.getMessage();
        String details = response.getDetails();
        message = message + ", details: " + details;

        switch (httpStatusCode) {
            case HttpStatus.SC_MOVED_TEMPORARILY: { // 302
                return new RedirectionException(code, category, message, httpStatusCode); // Used for redirection response in 3DS, Masterpass and Interac Online requests
            }
            case HttpStatus.SC_BAD_REQUEST: { // 400
                return new InvalidRequestException(code, category, message, httpStatusCode); // Often missing a required parameter
            }
            case HttpStatus.SC_UNAUTHORIZED: { // 401
                return new UnauthorizedException(code, category, message, httpStatusCode); // Authentication exception
            }
            case HttpStatus.SC_PAYMENT_REQUIRED: { // 402
                return new BusinessRuleException(code, category, message, httpStatusCode); // Request failed business requirements or rejected by processor/bank
            }
            case HttpStatus.SC_FORBIDDEN: { // 403
                return new ForbiddenException(code, category, message, httpStatusCode); // Authorization failure
            }
            case HttpStatus.SC_METHOD_NOT_ALLOWED: { // 405
                return new InvalidRequestException(code, category, message, httpStatusCode); // Sending the wrong HTTP Method
            }
            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE: { // 415
                return new InvalidRequestException(code, category, message, httpStatusCode); // Sending an incorrect Content-Type
            }
            default: {
                return new InternalServerException(code, category, message, httpStatusCode);
            }

        }
    }

    public int getCode() {
        return code;
    }

    public int getCategory() {
        return category;
    }
    
    /**
     * If this exception was cause by user input ie. invalid card number or declines.
     * Returns false for all other errors that were not caused by user input, such
     * as network timeouts, message formatting, etc.
     * @return true if the error was caused by user input or a decline.
     */
    public boolean isUserError() {
        return false;
    }
    
    /**
     * This message should be displayed to the card holder.
     * Overwritten in some sublcass exceptions.
     * @return a simple string message that can be displayed to the card holder.
     */
    public String getUserFacingMessage() {
        return "There was an error processing your request. Please try again or use a different card.";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+" code: "+code+" category: "+category+" message: "+message;
    }
    
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

}
