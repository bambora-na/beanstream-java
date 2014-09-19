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

    public ForbiddenException(int code, int category, String message, int httpStatusCode) {
        super(code, category, message, httpStatusCode);
    }
}