package com.beanstream.responses;

import com.beanstream.exceptions.BeanstreamApiException;
import com.google.common.net.MediaType;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class BeanstreamResponse {

    private final int code;
    private final int category;
    private final String message;
    private final String reference;
    private final String details;
    private final int httpStatusCode;
    private final String responseBody;
    private final MediaType mediaType;

    private static final Gson gson = new Gson();

    BeanstreamResponse(int code, int category, String message, String reference, String details, int httpStatusCode,
                       String responseBody, MediaType mediaType) {
        this.code = code;
        this.category = category;
        this.message = message;
        this.reference = reference;
        this.details = details;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
        this.mediaType = mediaType;
    }

    public static BeanstreamResponse fromException(BeanstreamApiException e) {
        return new BeanstreamResponseBuilder()
                .withCode(e.getCode())
                .withCategory(e.getCategory())
                .withMessage(e.getMessage())
                .withHttpStatusCode(e.getHttpStatusCode())
                .build();
    }

    public static BeanstreamResponse emptyResponse() {
        return new BeanstreamResponseBuilder().build();
    }

    public static BeanstreamResponse fromMessage(String message) {
        return new BeanstreamResponseBuilder()
                .withMessage(message)
                .build();
    }

    public static BeanstreamResponse fromHttpResponse(HttpResponse http) {

        int httpStatusCode = http.getStatusLine().getStatusCode();
        HttpEntity httpEntity = http.getEntity();
        String jsonPayload = null;

        try {
            jsonPayload = httpEntity != null ? EntityUtils.toString(httpEntity) : null;
        } catch (IOException e) {
            return new BeanstreamResponseBuilder()
                    .withHttpStatusCode(httpStatusCode)
                    .withMessage(e.getMessage())
                    .setMediaType(MediaType.ANY_TYPE)
                    .build();
        }
        
        String contentType = httpEntity.getContentType().getValue();
        MediaType responseType = MediaType.parse(contentType);

        // If the payload isn't json, or we got a 2XX response, just populate the responseBody field with the payload
        if (responseType != MediaType.JSON_UTF_8 || (httpStatusCode >= 200 && httpStatusCode < 300)) {
            return new BeanstreamResponseBuilder()
                    .withHttpStatusCode(httpStatusCode)
                    .withResponseBody(jsonPayload)
                    .setMediaType(responseType)
                    .build();
        }

        // else get the usual code/cat/msg/ref response
        return fromJson(httpStatusCode, jsonPayload, responseType);
    }

    private static BeanstreamResponse fromJson(int httpStatusCode, String jsonPayload, MediaType responseType) {

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonPayload).getAsJsonObject();
        BeanstreamResponseBuilder builder = new BeanstreamResponseBuilder();

        builder.setMediaType(responseType);
        
        // for now, need to guard for nulls
        JsonElement element = null;
        element = json.get("code");
        if (!element.isJsonNull()) {
            builder.withCode(element.getAsInt());
        }

        element = json.get("category");
        if (!element.isJsonNull()) {
            builder.withCategory(element.getAsInt());
        }

        element = json.get("message");
        if (!element.isJsonNull()) {
            builder.withMessage(element.getAsString());
        }

        element = json.get("reference");
        if (!element.isJsonNull()) {
            builder.withReference(element.getAsString());
        }
        
        element = json.get("details");
        if (element != null && !element.isJsonNull()) {
            builder.withDetails(element.toString());
        }

        builder.withHttpStatusCode(httpStatusCode);
        return builder.build();
    }

    public int getCode() {
        return code;
    }

    public int getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public String getReference() {
        return reference;
    }

    public String getDetails() {
        return details;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BeanstreamResponse response = (BeanstreamResponse) o;

        if (category != response.category) {
            return false;
        }
        if (code != response.code) {
            return false;
        }
        if (httpStatusCode != response.httpStatusCode) {
            return false;
        }
        if (message != null ? !message.equals(response.message) : response.message != null) {
            return false;
        }
        if (reference != null ? !reference.equals(response.reference) : response.reference != null) {
            return false;
        }
        if (details != null ? !details.equals(response.details) : response.details != null) {
            return false;
        }
        if (responseBody != null ? !responseBody.equals(response.responseBody) : response.responseBody != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + category;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + httpStatusCode;
        result = 31 * result + (responseBody != null ? responseBody.hashCode() : 0);
        return result;
    }


}
