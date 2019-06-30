package com.project.banking.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.banking.account.controller.AccountController;

/**
 * Main application to run the AccountController
 *
 */
public class AccountApplication {

	static Logger logger = LoggerFactory.getLogger(AccountApplication.class);

	public static void main(String[] args) {
		logger.info("<=><=><=><=><=> Starting AccountController <=><=><=><=><=>");
		AccountController.invokeController();
	}
}
