package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// Http status codes:
///  402 - Processing Error - Request failed business requirements or rejected by processor/bank.
///
///
/// </summary>
public class BusinessRuleException extends BeanstreamApiException {

    public BusinessRuleException(int statusCode, String response, int category, int code) {
        super(statusCode, response, category, code);
    }
}