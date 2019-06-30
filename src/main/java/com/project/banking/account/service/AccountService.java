package com.project.banking.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.project.banking.account.exception.AccountNotFoundException;
import com.project.banking.account.exception.InsufficientFundException;
import com.project.banking.account.model.Account;

public interface AccountService {

	public Long createAccount(Account account);
	
	public void deleteAccount(Long id) throws AccountNotFoundException;

	public void deleteAllAccounts();

	public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException;
	
	public BigDecimal creditMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException;
	
	public BigDecimal debitMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException;
	
	public BigDecimal getBalance(Long accountId) throws AccountNotFoundException;
	
	public Account getAccount(Long id) throws AccountNotFoundException;
	
	public List<Account> getAllAccounts();
	
}
