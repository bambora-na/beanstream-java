package com.beanstream.exceptions;

/**
 * Created by michael on 9/16/14.
 */
public class CommunicationException extends Exception {

    public CommunicationException(String message, Exception exception) {
        super(message, exception);
    }
}