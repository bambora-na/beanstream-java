package com.beanstream.responses;

import com.google.gson.Gson;

public class BeanstreamResponse {
	public final int code;
	public final String category;
	public final String message;
	public final String reference;
	private static final Gson gson = new Gson();

	public BeanstreamResponse(int code, String category, String message,
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
				response = new BeanstreamResponse(0, null, null, null);
			}
		} catch (Exception ex) {

		}
		this.code = response.code;
		this.category = response.category;
		this.message = response.message;
		this.reference = response.reference;

	}

}
