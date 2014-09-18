package com.beanstream.responses;

import com.beanstream.exceptions.BeanstreamApiException;
import com.google.gson.Gson;

public class BeanstreamResponse {
	public int code;
	public int category;
	public String message;
	public String reference;
    public int httpStatusCode;
    public String responseBody;
    private static final Gson gson = new Gson();

    public BeanstreamResponse() {}

    public BeanstreamResponse(int code, int category, String message,
			String reference) {
		super();
		this.code = code;
		this.category = category;
		this.message = message;
		this.reference = reference;
	}

	public BeanstreamResponse(String jsonObject) {
		super();
		BeanstreamResponse response = null;
		try {
			response = gson.fromJson(jsonObject, BeanstreamResponse.class);
			if (response == null) {
				response = new BeanstreamResponse(0, 0, null, null);
			}
		} catch (Exception ex) {

		}
		this.code = response.code;
		this.category = response.category;
		this.message = response.message;
		this.reference = response.reference;
	}

    public static BeanstreamResponse fromJson(String json) {
        return gson.fromJson(json, BeanstreamResponse.class);
    }

    public static BeanstreamResponse fromBeanstreamException(BeanstreamApiException e) {
        return new BeanstreamResponse(e.getCode(), e.getCategory(), e.getResponseMessage(), null);
    }

    public static BeanstreamResponse emptyResponse() {
        return new BeanstreamResponse(-1, -1, "", "");
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanstreamResponse other = (BeanstreamResponse) obj;
		if (category != other.category)
            return false;
		if (code != other.code)
            return false;
		return true;
	}

}
