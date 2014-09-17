package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// Http status codes:
///  403 - Forbidden - Unauthorized access.
///
///
/// </summary>
public class ForbiddenException extends BeanstreamApiException {

    public ForbiddenException(int statusCode, String response, int category, int code) {
        super(statusCode, response, category, code);
    }
}