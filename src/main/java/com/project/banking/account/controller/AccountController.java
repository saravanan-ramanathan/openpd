package com.project.banking.account.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.port;
import static spark.Spark.post;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.project.banking.account.exception.AccountNotFoundException;
import com.project.banking.account.exception.InsufficientFundException;
import com.project.banking.account.model.Account;
import com.project.banking.account.model.Transfer;
import com.project.banking.account.service.AccountService;
import com.project.banking.account.service.AccountServiceImpl;

public class AccountController {

	static Logger logger = LoggerFactory.getLogger(AccountController.class);
	static Gson gson = new Gson();

	public static void invokeController() {
		final AccountService accountService = new AccountServiceImpl();
		String contentType = "application/json";
		
		// Port Configuration
		port(8085);

		// Create Account
		post("/account/v1/create", (req, res)-> {
			res.type(contentType);
			Account account = gson.fromJson(req.body(), Account.class);
			Long accountId = accountService.createAccount(account);
			logger.info("Account created : " + accountId);
			return gson.toJson(accountId);
		});

		// Delete Account
		delete("/account/v1/delete/:id", (req, res) -> {
			res.type(contentType);
			Long accountId = new Long(req.params(":id"));
			try {
				accountService.deleteAccount(accountId);
			} catch(AccountNotFoundException aNFExc) {
				return gson.toJson(aNFExc.getMessage());
			}
			logger.info("Account deleted : " + accountId);
			return gson.toJson("Account " + accountId + " deleted!");
		});

		// Delete All Accounts
		delete("/account/v1/deleteall", (req, res) -> {
			res.type(contentType);
			accountService.deleteAllAccounts();
			return gson.toJson("All the accounts are deleted!");
		});

		// Transfer Money to Account
		patch("/account/v1/transfer", (req, res) -> {
			res.type(contentType);
			Transfer transfer = gson.fromJson(req.body(), Transfer.class);
			try {
				accountService.transferMoney(transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getAmount());
			} catch(AccountNotFoundException | InsufficientFundException exc) {
				return gson.toJson(exc.getMessage());
			}
			logger.info("Amount transferred successfully!");
			return gson.toJson("Amount transferred successfully!");
		});

		// Credit Account
		patch("/account/v1/credit", (req, res) -> {
			res.type(contentType);
			Transfer transfer = gson.fromJson(req.body(), Transfer.class);
			logger.info("Credit, transfer Object : " + transfer);
			BigDecimal newBalance = null;
			try {
				newBalance = accountService.creditMoney(transfer.getToAccountId(), transfer.getAmount());
				logger.info("Amount credited to account " + transfer.getToAccountId() + ", newBalance=" + newBalance);
			} catch(AccountNotFoundException exc) {
				return gson.toJson(exc.getMessage());
			}
			return gson.toJson(newBalance);
		});

		// Debit Account
		patch("/account/v1/debit", (req, res) -> {
			res.type(contentType);
			Transfer transfer = gson.fromJson(req.body(), Transfer.class);
			BigDecimal newBalance = null;
			try {
				newBalance = accountService.debitMoney(transfer.getFromAccountId(), transfer.getAmount());
				logger.info("Amount debited from account " + transfer.getFromAccountId() + ", newBalance=" + newBalance);
			} catch(AccountNotFoundException | InsufficientFundException exc) {
				return gson.toJson(exc.getMessage());
			}
			return gson.toJson(newBalance);
		});
		
		// Get Account Balance
		get("/account/v1/balance/:id", (req, res) -> {
			res.type(contentType);
			BigDecimal balance = null;
			try {
				balance = accountService.getBalance(new Long(req.params(":id")));
			} catch(AccountNotFoundException exc) {
				return gson.toJson(exc.getMessage());
			}		
			return gson.toJson(balance);
		});
			
		// Get Account Details
		get("/account/v1/account/:id", (req, res) -> {
			res.type(contentType);
			Account account = null;
			try {
				account = accountService.getAccount(new Long(req.params(":id")));
			} catch(AccountNotFoundException exc) {
				return gson.toJson(exc.getMessage());
			}		
			return gson.toJson(account);
		});

		// Get All Accounts
		get("/account/v1/accounts", (req, res) -> {
			res.type(contentType);
			return gson.toJson(accountService.getAllAccounts());
		});
	}

}
