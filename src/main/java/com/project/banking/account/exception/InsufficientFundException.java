package com.project.banking.account.exception;

public class InsufficientFundException extends Throwable {

	private static final long serialVersionUID = 8618551929850530063L;

	private String message = null;
	
	public InsufficientFundException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
