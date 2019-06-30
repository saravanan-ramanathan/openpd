package com.project.banking.account.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.banking.account.controller.AccountController;
import com.project.banking.account.exception.AccountNotFoundException;
import com.project.banking.account.exception.InsufficientFundException;
import com.project.banking.account.model.Account;
import com.project.banking.account.util.NumberGenerator;

public class AccountRepository {

	private HashMap<Long, Account> inMemoryAccountHolder = new HashMap<>();
	static Logger logger = LoggerFactory.getLogger(AccountController.class);

	public Long createAccount(Account account) {
		logger.info("Create Account Before : " + inMemoryAccountHolder);
		Long accountId = NumberGenerator.getNextNumber();
		if(account != null) {
			account.setId(accountId);
			inMemoryAccountHolder.put(accountId, account);
		}
		logger.info("Create Account After : " + inMemoryAccountHolder);
		return accountId;
	}

	public void deleteAccount(Long accountId) throws AccountNotFoundException {
		logger.info("Delete Account Before : " + inMemoryAccountHolder);
		if(accountId != null) {
			getAccount(accountId);
			inMemoryAccountHolder.remove(accountId);
		}
		logger.info("Delete Account After : " + inMemoryAccountHolder);
	}

	public void deleteAllAccounts() {
		logger.info("Delete All Accounts Before : " + inMemoryAccountHolder);
		inMemoryAccountHolder.clear();
		logger.info("Delete All Accounts After : " + inMemoryAccountHolder);
	}

	public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException {
		logger.info("Transfer Money Before : " + inMemoryAccountHolder);
		if(fromAccountId != null && toAccountId != null && amount != null) {
			debitMoney(fromAccountId, amount);
			creditMoney(toAccountId, amount);
		}
		logger.info("Transfer Money After : " + inMemoryAccountHolder);
	}

	public BigDecimal creditMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException {
		logger.info("Credit Money Before : " + inMemoryAccountHolder);
		BigDecimal newAccountBal = null;
		if(accountId != null & amount != null) {
			Account account = getAccount(accountId);
			BigDecimal accountBal = account.getBalance();
			newAccountBal = accountBal.add(amount);
			account.setBalance(newAccountBal);
		}
		logger.info("Credit Money After : " + inMemoryAccountHolder);
		return newAccountBal;
	}

	public BigDecimal debitMoney(Long accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundException {
		logger.info("Debit Money Before : " + inMemoryAccountHolder);
		BigDecimal newAccountBal = null;
		if(accountId != null & amount != null) {
			Account account = getAccount(accountId);
			BigDecimal accountBal = account.getBalance();
			if(accountBal.compareTo(amount) < 0) {
				throw new InsufficientFundException("Account " + accountId + " does not have sufficient balance.");
			}
			newAccountBal = accountBal.subtract(amount);
			account.setBalance(newAccountBal);
		}
		logger.info("Debit Money After : " + inMemoryAccountHolder);
		return newAccountBal;
	}

	public BigDecimal getBalance(Long accountId) throws AccountNotFoundException {
		logger.info("Get Balance : " + inMemoryAccountHolder);
		BigDecimal balance = null;
		if(accountId != null) {
			Account account = getAccount(accountId);
			balance = account.getBalance();
		}
		return balance;
	}

	public Account getAccount(Long accountId) throws AccountNotFoundException {
		logger.info("Get Account : " + inMemoryAccountHolder);
		Account account = null;
		if(accountId != null) {
			account = inMemoryAccountHolder.get(accountId);
		}
		
		if(account == null) {
			throw new AccountNotFoundException("Account not found " + accountId);
		}
		
		return account;
	}

	public List<Account> getAllAccounts() {
		logger.info("Get Accounts : " + inMemoryAccountHolder);
		List<Account> accountList = inMemoryAccountHolder.values()
				.stream()
				.collect(Collectors.toList());
		return accountList;
	}

}
