package com.beanstream.responses;

public class Detail {
    private final String field;
    private final String message;
    public Detail(String field, String message) {
    	this.field = field;
    	this.message = message;
    }
	public String getField() {
		return field;
	}
	public String getMessage() {
		return message;
	}
}
