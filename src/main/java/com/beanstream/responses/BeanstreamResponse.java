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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
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
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (code != other.code)
			return false;
		return true;
	}

}
