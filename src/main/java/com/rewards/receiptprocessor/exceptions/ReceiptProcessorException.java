package com.rewards.receiptprocessor.exceptions;

import org.springframework.http.HttpStatus;

public class ReceiptProcessorException extends Exception {
	
	private HttpStatus errorCode;
	private String description;
	
	public ReceiptProcessorException(String message, HttpStatus errorCode, String description) {
		
		super(message);
		
		this.setErrorCode(errorCode);
		this.setDescription(description);
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
