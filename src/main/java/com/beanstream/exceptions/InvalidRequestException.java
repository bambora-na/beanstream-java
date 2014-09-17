package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// The request can be invalid for several reasons:
/// Http status codes:
///  400 - Bad Request - Often missing a required parameter
///  405 - Method not allowed - Sending the wrong HTTP Method
///  415 - Unsupported Media Type - Sending an incorrect Content-Type
///
/// This error should not occur while in a production environment. If it occurs the developer
/// has done something wrong and the cardholder or merchant getting this message should contact the developer
/// of the software.
/// </summary>
public class InvalidRequestException extends BeanstreamApiException {

    public InvalidRequestException(int statusCode, String response, int category, int code) {
        super(statusCode, response, category, code);
    }
}