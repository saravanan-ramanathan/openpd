package com.project.banking.account;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.banking.account.model.Account;
import com.project.banking.account.util.NumberGenerator;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * API testing is done using Rest Assured & JUnit 
 * @author sarav
 *
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountAPITest {

	private static Logger logger = LoggerFactory.getLogger(AccountAPITest.class);
	private static String testURL = null;
	private static Gson gson = new Gson();
	private static String contentType = "application/json";
	private static Long accountId = null;

	@BeforeClass
	public static void setup() {
		RestAssured.port = 8085;
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/account/v1/";
		testURL = RestAssured.baseURI + ":" + RestAssured.port + RestAssured.basePath;
		logger.info("--->>>>> testURL : " + testURL);
	}
	
	@Test
	public void test1_WhenTheBaseURLIsInvokedItShouldReturn404StatusCode() {
		given()
			.when()
			.get(testURL)
			.then()
			.statusCode(404);
	}
	
	@Test
	public void test2_WhenInvokedInitiallyGetAllAccountsMethodShouldReturnEmptyList() {
		Response response = given()
			.when()
			.get(testURL + "accounts")
			.then()
			.extract()
			.response();

		logger.info("Response <><><><> : " + response.asString());
		
		List<Account> accountList = gson.fromJson(response.asString(), new TypeToken<ArrayList<Account>>(){}.getType());
		logger.info("List accounts size : " + accountList.size());
		
		assertThat(0, is(accountList.size()));
	}
	
	@Test
	public void test3_WhenNewAccountIsCreatedItShouldReturnTheNewAccountNumber() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", "application/json");
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "Har");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "1200");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		accountId = new Long(response.asString());
		logger.info("New AccountId : " + accountId);
		
		assertNotNull(accountId);
	}
	
	@Test
	public void test4_WhenDeletingAnAccountIfItIsAValidAccountThenItShouldGetDeletedWithStatusCode200() {
		logger.info("In Delete accountId : " + accountId);
		given().contentType(contentType)
			.when()
			.delete(testURL + "delete/" + accountId)
			.then()
			.statusCode(200);
	}
	
	@Test
	public void test5_WhenDeletingAnAccountIfItIsAnIValidAccountThenItShouldThrowAccountNotFoundException() {
		logger.info("In Deleting Invalid account...");

		Long invalidAccountId = NumberGenerator.getNextNumber();
		Response response = given()
				.when()
				.delete(testURL + "delete/" + invalidAccountId)
				.then()
				.extract()
				.response();

		logger.info("Invalid delete <><><><> : " + response.asString());
		assertThat(("\"Account not found " + invalidAccountId + "\""), is(response.asString()));
	}

	private Long setupTestDataForTest6() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "John");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "700");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test6 accountId : " + accountId);
		return accountId;
	}
	
	@Test
	public void test6_WhenCreditingMoneyToAnAccountItShouldIncreaseTheBalanceOfTheAccountWithTheAmountCredited() {
		Long accountId = setupTestDataForTest6();
		
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fromAccountId", new Long(0));
		jsonObject.addProperty("toAccountId", accountId);
		jsonObject.addProperty("amount", new BigDecimal("200"));
		reqSpec.body(jsonObject.toString());
		logger.info("Amount credit jsonObject : " + jsonObject.toString());
		
		Response response = reqSpec.patch(testURL + "credit");
		logger.info("Amount credited, newBalance : " + response.asString());
		
		assertThat("900", is(response.asString()));
	}
	
	private Long setupTestDataForTest7() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "Kevin");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "4400");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test7 accountId : " + accountId);
		return accountId;
	}
	
	@Test
	public void test7_WhenDebitingMoneyFromAnAccountItShouldDecreaseTheBalanceOfTheAccountWithTheAmountDebited() {
		Long accountId = setupTestDataForTest7();
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fromAccountId", accountId);
		jsonObject.addProperty("toAccountId", new Long(0));
		jsonObject.addProperty("amount", new BigDecimal("500"));
		reqSpec.body(jsonObject.toString());
		logger.info("Amount debit jsonObject : " + jsonObject.toString());
		
		Response response = reqSpec.patch(testURL + "debit");
		logger.info("Amount debited, newBalance : " + response.asString());
		
		assertThat("3900", is(response.asString()));
	}

	private Long setupTestDataForTest8() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "Mark");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "5600");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test8 accountId : " + accountId);
		return accountId;
	}

	@Test
	public void test8_WhenDebitingMoneyFromAnAccountItShouldThrowInsufficientFundExceptionIfTheBalanceIsNotSufficient() {
		Long accountId = setupTestDataForTest8();
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fromAccountId", accountId);
		jsonObject.addProperty("toAccountId", new Long(0));
		jsonObject.addProperty("amount", new BigDecimal("5800"));
		reqSpec.body(jsonObject.toString());
		logger.info("Amount debit for insufficient balance jsonObject : " + jsonObject.toString());
		
		Response response = reqSpec.patch(testURL + "debit");
		logger.info("Amount debited for insufficient balance testing, newBalance : " + response.asString());
		
		assertThat(("\"Account " + accountId + " does not have sufficient balance.\""), is(response.asString()));
	}

	private Long setupTestDataForTest9Debit() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "AccountDebit");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "5600");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test9 Debit accountId : " + accountId);
		return accountId;
	}
	
	private Long setupTestDataForTest9Credit() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "AccountCredit");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "4200");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test9 Credit accountId : " + accountId);
		return accountId;
	}

	@Test
	public void test9_WhenTransferringMoneyFromOneAccountToOtherAccountBalanceShouldInCreaseAndDecreaseInTheRespectiveAccounts() {
		Long debitAccountId = setupTestDataForTest9Debit();
		Long creditAccountId = setupTestDataForTest9Credit();
		
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fromAccountId", debitAccountId);
		jsonObject.addProperty("toAccountId", creditAccountId);
		jsonObject.addProperty("amount", new BigDecimal("2000"));
		reqSpec.body(jsonObject.toString());
		logger.info("Amount transfer jsonObject : " + jsonObject.toString());

		Response response = reqSpec.patch(testURL + "transfer");
		logger.info("Amount transfer response : " + response.asString());
		
		assertThat(("\"Amount transferred successfully!\""), is(response.asString()));
	}

	private Long setupTestDataForTest10Debit() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "AccountDebitInSuf");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "800");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test10 Debit accountId : " + accountId);
		return accountId;
	}
	
	private Long setupTestDataForTest10Credit() {
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "AccountCreditInSuf");
		jsonObject.addProperty("currency", "GBP");
		jsonObject.addProperty("balance", "1200");
		reqSpec.body(jsonObject.toString());
		
		Response response = reqSpec.post(testURL + "create");
		Long accountId = new Long(response.asString());
		logger.info("Test10 Credit accountId : " + accountId);
		return accountId;
	}

	@Test
	public void testA10_WhenTransferringMoneyFromOneAccountToOtherAccountAndIfTheBalanceIsInsufficientThenTransactionShouldFailWithInsufficientFundException() {
		Long debitAccountId = setupTestDataForTest10Debit();
		Long creditAccountId = setupTestDataForTest10Credit();
		
		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.header("Content-Type", contentType);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fromAccountId", debitAccountId);
		jsonObject.addProperty("toAccountId", creditAccountId);
		jsonObject.addProperty("amount", new BigDecimal("900"));
		reqSpec.body(jsonObject.toString());
		logger.info("Amount transfer jsonObject : " + jsonObject.toString());

		Response response = reqSpec.patch(testURL + "transfer");
		logger.info("Amount transfer Insufficient fund testing response : " + response.asString());
		
		assertThat(("\"Account " + debitAccountId + " does not have sufficient balance.\""), is(response.asString()));
	}

	@Test
	public void testB11_WhenGettingTheAccountDetailsOfAllAccountsThenItShouldReturnTheListOfAllAccounts() {
		Response response = given()
				.when()
				.get(testURL + "accounts")
				.then()
				.extract()
				.response();

		logger.info("Response <><><><> : " + response.asString());
			
		List<Account> accountList = gson.fromJson(response.asString(), new TypeToken<ArrayList<Account>>(){}.getType());
		logger.info("List accounts size : " + accountList.size());
			
		assertThat(accountList, is(notNullValue()));
	}
	
	@AfterClass
	public static void teardown() {
		logger.info("In teardown, deleting all test data...");
		given().contentType(contentType)
			.when()
			.delete(testURL + "deleteall");
	}
}
