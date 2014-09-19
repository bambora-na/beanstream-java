package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// Http status codes:
///  401 - Unauthorized - No valid authentication provided.
///
/// The merchant API key may be wrong or improperly formatted and encoded.
///
/// This error should not occur while in a production environment. If it occurs the developer
/// has done something wrong and the cardholder or merchant getting this message should contact the developer
/// of the software.
/// </summary>
public class UnauthorizedException extends BeanstreamApiException {

    public UnauthorizedException(int code, int category, String message, int httpStatusCode) {
        super(code, category, message, httpStatusCode);
    }
}