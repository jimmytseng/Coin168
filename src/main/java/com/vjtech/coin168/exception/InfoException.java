package com.vjtech.coin168.exception;

public class InfoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

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

	public InfoException(String message) {
		this.message= message;
	}

	public InfoException(String code, String message) {
		this.message= message;
		this.code = code;
	}

	public InfoException(String code, String message, Throwable cause) {
		super(message,cause);
		this.message= message;
		this.code = code;
	}
}
