package com.project.banking.account.model;

import java.math.BigDecimal;

public class Account {

	private Long id;
	private String name;
	private String currency;
	private BigDecimal balance;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		StringBuilder stringBldr = new StringBuilder();
		stringBldr.append("[id=" + id);
		stringBldr.append(", name=" + name);
		stringBldr.append(", currency=" + currency);
		stringBldr.append(", balance=" + balance + "]");
		return stringBldr.toString();
	}

}
