package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
/// <summary>
/// An error occurred on the Beanstream servers.
///
/// Http status codes:
///  500 - Internal Server Error - Application error on Beanstream's end
///  503 - Service Unavailable - Beanstream service is unavailable
///  504 - Gateway Timeout - Beanstream service timed out
///
/// This error should be handled neatly and ask the user to try their request again in a moment.
///
/// If the Beanstream network is down it will redirect to one of several failover data centres. Each failover
/// has its own IP address that you will need to obtain by calling Beanstream's technical support. You will
/// also have to make sure your firewall will allow a connection out to these new IP addresses.
/// </summary>
public class InternalServerException extends BeanstreamApiException {

    public InternalServerException(int code, int category, String message, int httpStatusCode) {
        super(code, category, message, httpStatusCode);
    }
}