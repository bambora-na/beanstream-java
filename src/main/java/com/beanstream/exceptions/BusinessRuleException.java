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

    public BusinessRuleException(int code, int category, String message, int httpStatusCode) {
        super(code, category, message, httpStatusCode);
    }
}