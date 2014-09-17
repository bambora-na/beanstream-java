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
 */
public class BeanstreamApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private int statusCode;
    private String responseMessage;
    private int category;
    private int code;

    protected BeanstreamApiException(int statusCode, String responseMessage, int category, int code) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.category = category;
        this.code = code;
    }

    public BeanstreamApiException(Exception e, String responseMessage) {
        super(e);
        this.responseMessage = responseMessage;
    }

    public static BeanstreamApiException getMappedException(int statusCode) {
        return getMappedException(statusCode, BeanstreamResponse.emptyResponse());
    }

    public static BeanstreamApiException getMappedException(int statusCode, BeanstreamResponse err) {
        String response = err.message;
        int category = err.category;
        int code = err.code;

        switch (statusCode) {
            case HttpStatus.SC_MOVED_TEMPORARILY: { // 302
                return new RedirectionException(statusCode, response, category, code); // Used for redirection response in 3DS, Masterpass and Interac Online requests
            }
            case HttpStatus.SC_BAD_REQUEST: { // 400
                return new InvalidRequestException(statusCode, response, category, code); // Often missing a required parameter
            }
            case HttpStatus.SC_UNAUTHORIZED: { // 401
                return new UnauthorizedException(statusCode, response, category, code); // Authentication exception
            }
            case HttpStatus.SC_PAYMENT_REQUIRED: { // 402
                return new BusinessRuleException(statusCode, response, category, code); // Request failed business requirements or rejected by processor/bank
            }
            case HttpStatus.SC_FORBIDDEN: { // 403
                return new ForbiddenException(statusCode, response, category, code); // Authorization failure
            }
            case HttpStatus.SC_METHOD_NOT_ALLOWED: { // 405
                return new InvalidRequestException(statusCode, response, category, code); // Sending the wrong HTTP Method
            }
            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE: { // 415
                return new InvalidRequestException(statusCode, response, category, code); // Sending an incorrect Content-Type
            }
            default:
                return new InternalServerException(statusCode, response, category, code);


        }
    }

	public String getResponseMessage() {
		return responseMessage;
	}

	public int getHttpStatusCode() {
		return statusCode;
	}

    public int getCategory() {
        return category;
    }

    public int getCode() {
        return code;
    }

}