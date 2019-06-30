package com.project.banking.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.project.banking.account.exception.AccountNotFoundException;
import com.project.banking.account.exception.InsufficientFundException;
import com.project.banking.account.model.Account;
import com.project.banking.account.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository = new AccountRepository();
	
	public Long createAccount(Account account) {
		return accountRepository.createAccount(account);
	}
	
	public void deleteAccount(Long accountId) throws AccountNotFoundException {
		accountRepository.deleteAccount(accountId);
	}

	public void deleteAllAccounts() {
		accountRepository.deleteAllAccounts();
	}

	public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException {
		accountRepository.transferMoney(fromAccountId, toAccountId, amount);
	}
	
	public BigDecimal creditMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException {
		return accountRepository.creditMoney(accountId, amount);
	}
	
	public BigDecimal debitMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException {
		return accountRepository.debitMoney(accountId, amount);
	}
	
	public BigDecimal getBalance(Long accountId) throws AccountNotFoundException {
		return accountRepository.getBalance(accountId);
	}
	
	public Account getAccount(Long accountId) throws AccountNotFoundException {
		return accountRepository.getAccount(accountId);
	}
	
	public List<Account> getAllAccounts() {
		return accountRepository.getAllAccounts();
	}

}
