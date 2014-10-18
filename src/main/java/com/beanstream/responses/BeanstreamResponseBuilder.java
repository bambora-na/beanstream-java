package com.beanstream.responses;

import com.google.common.net.MediaType;

public class BeanstreamResponseBuilder {

    private int code = -1;
    private int category = -1;
    private String message = "";
    private String reference = "";
    private String details = "";
    private int httpStatusCode = -1;
    private String responseBody = "";
    private MediaType mediaType = null;

    public BeanstreamResponseBuilder withCode(int code) {
        this.code = code;
        return this;
    }

    public BeanstreamResponseBuilder withCategory(int category) {
        this.category = category;
        return this;
    }

    public BeanstreamResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public BeanstreamResponseBuilder withReference(String reference) {
        this.reference = reference;
        return this;
    }

    public BeanstreamResponseBuilder withHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    public BeanstreamResponseBuilder withResponseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public BeanstreamResponseBuilder withDetails(String details) {
        this.details = details;
        return this;
    }

    public BeanstreamResponse build() {
        return new BeanstreamResponse(code, category, message, reference, details, httpStatusCode, responseBody, mediaType);
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public BeanstreamResponseBuilder setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }
    
    
}