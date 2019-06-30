package com.project.banking.account.exception;

public class AccountNotFoundException extends Throwable {

	private static final long serialVersionUID = -8461272552252052121L;

	private String message = null;
	
	public AccountNotFoundException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
