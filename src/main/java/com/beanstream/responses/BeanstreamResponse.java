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
    private final int httpStatusCode;
    private final String responseBody;

    private static final Gson gson = new Gson();

    BeanstreamResponse(int code, int category, String message, String reference, int httpStatusCode,
                       String responseBody) {
        this.code = code;
        this.category = category;
        this.message = message;
        this.reference = reference;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
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
                    .build();
        }

        String contentType = httpEntity.getContentType().getValue();
        MediaType responseType = MediaType.parse(contentType);

        // If the payload isn't json, or we got a 2XX response, just populate the responseBody field with the payload
        if (responseType != MediaType.JSON_UTF_8 || (httpStatusCode >= 200 && httpStatusCode < 300)) {
            return new BeanstreamResponseBuilder()
                    .withHttpStatusCode(httpStatusCode)
                    .withResponseBody(jsonPayload)
                    .build();
        }

        // else get the usual code/cat/msg/ref response
        return fromJson(httpStatusCode, jsonPayload);
    }

    private static BeanstreamResponse fromJson(int httpStatusCode, String jsonPayload) {

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonPayload).getAsJsonObject();
        BeanstreamResponseBuilder builder = new BeanstreamResponseBuilder();

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

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
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
        result = 31 * result + httpStatusCode;
        result = 31 * result + (responseBody != null ? responseBody.hashCode() : 0);
        return result;
    }


}
