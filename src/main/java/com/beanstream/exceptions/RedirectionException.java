package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// Http status codes:
///  302 - Found - Used for redirection response in 3DS, Masterpass and Interac Online requests
///
/// </summary>
public class RedirectionException extends BeanstreamApiException {

    public RedirectionException(int statusCode, String response, int category, int code) {
        super(statusCode, response, category, code);
    }
}