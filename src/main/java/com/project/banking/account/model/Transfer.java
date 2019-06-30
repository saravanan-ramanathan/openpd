package com.project.banking.account.model;

import java.math.BigDecimal;

public class Transfer {

	private Long fromAccountId;
	private Long toAccountId;
	private BigDecimal amount;
	
	public Long getFromAccountId() {
		return fromAccountId;
	}
	
	public void setFromAccountId(Long fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	
	public Long getToAccountId() {
		return toAccountId;
	}
	
	public void setToAccountId(Long toAccountId) {
		this.toAccountId = toAccountId;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBldr = new StringBuilder();
		stringBldr.append("[fromAccoutnId=" + fromAccountId);
		stringBldr.append(", toAccountId=" + toAccountId);
		stringBldr.append(", amount=" + amount + "]");
		return stringBldr.toString();
	}
}
