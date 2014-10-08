package com.beanstream.responses;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
	
	protected String code;
	protected String message;
	@SerializedName("customer_code")
	protected String id;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
