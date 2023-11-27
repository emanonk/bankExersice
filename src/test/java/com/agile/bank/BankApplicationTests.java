package com.agile.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Currency;

@SpringBootTest
class BankApplicationTests {

	@Test
	void contextLoads() {


//		currency.
		System.out.println(Currency.getAvailableCurrencies());
	}

	/*
	POST /v1/accounts/
	GET  /v1/accounts/:id
	POST /v1/accounts/:id

	GET  /v1/accounts

	POST /v1/transfers
	GET  /v1/transfers/:id
	POST /v1/transfers/:id


	Assumptions:
	- For simplicity it is fine for the account number to be the db id, in the real world that is not secure
	- In the exercise there is one example with GBP and one with EUR, which means that the bank accepts transfers for more than one currency,
	  so for simplicity again if the user does not have a wallet for this currency then the transaction will be declined
	  An alternative solution could be a new wallet(currency) to be created.
	  Or the system to convert the source currency to the currency that the target account supports with a real-time exchange rate and probably a fee.
	- In the real world, banking systems use event sourcing for auditing purposes, but for simplicity I will not implement an event sourcing approach for this exercise.
	- There is not need for Users as in the real world there will be another microservice which is responsible for the Users/Accounts relationship

	- No need for findAll as it was not requested in the acceptance criteria
	- Using MySQL is important to annotate methods with Transactional to make sure that the debit and credit requests are under the same transaction
	- Create account and all other account facade methods are on purpose not exposed as there was not included in the requirements, the facade methods are created as in order to make a transaction 2 accounts are needed.
	- Did not log anything on purpose, as it is a financial service and logs can not contain details about customer data
	- I did not implement any api adapter in the account package, as there is no requirement for that functionality


	http://localhost:8080/swagger-ui/index.html

	 */


}
