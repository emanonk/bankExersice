package com.agile.bank;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.transaction.TransactionFacade;
import com.agile.bank.transaction.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
class BankApplicationTests {

    private static final Currency USD = Currency.getInstance("USD");
    private static final String TRANSACTION_FAILED_INSUFFICIENT = "Transaction failed, source account : %s and target account : %s, with currency: USD, with error: Account with id: %s has insufficient balance for the transfer request";
    private static final String TRANSACTION_FAILED_TRANSFERS_ARE_NOT_ALLOWED_IN_THE_SAME_ACCOUNT = "Transaction failed, source account : %s and target account : %s, with error: Transfers are not allowed in the same account";
    public static final String TRANSACTION_FAILED_WITH_ERROR_ACCOUNT_NOT_FOUND = "Transaction failed, source account : %s and target account : %s, with currency: USD, with error: Account with id: %s not found";
    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private TransactionFacade transactionFacade;

    @Test
    void moneyTransferBetweenTwoAccounts() {
        //Given source account exists
        //and target account exists
        //and source account has a balance greater or equal to the transaction amount
        Long sourceAccountId = accountFacade.createAccount(new BigDecimal("5000.00"), USD);
        Long targetAccountId = accountFacade.createAccount(new BigDecimal("1000.00"), USD);

        //When a transaction request is received
        Transaction transaction = Transaction.builder()
                .currency(USD)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .amount(new BigDecimal("4999.00"))
                .build();

        transactionFacade.applyTransfer(transaction);

        //Then the balance of source account should be debited
        //and the balance of target account should be credited
        BigDecimal sourceAccountBalance = accountFacade.getAccountBalance(sourceAccountId);
        BigDecimal targetAccountBalance = accountFacade.getAccountBalance(targetAccountId);

        assertThat(sourceAccountBalance).isEqualTo(new BigDecimal("1.00"));
        assertThat(targetAccountBalance).isEqualTo(new BigDecimal("5999.00"));
    }

    @Test
    void insufficientBalanceToProcessMoneyTransfer() {
        //Given source account exists
        //and target account exists
        //and source account has a balance less than the transaction amount
        Long sourceAccountId = accountFacade.createAccount(new BigDecimal("1000.00"), USD);
        Long targetAccountId = accountFacade.createAccount(new BigDecimal("1000.00"), USD);

        //When a transaction request is received
        //Then the balance of source account should remain the same and the
        //balance of target account should remain the sameand the
        //client of the API should receive an error
        Transaction transaction = Transaction.builder()
                .currency(USD)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .amount(new BigDecimal("5000.00"))
                .build();

        String errorMessage = String.format(TRANSACTION_FAILED_INSUFFICIENT, sourceAccountId, targetAccountId, sourceAccountId);

        assertThatThrownBy(() -> transactionFacade.applyTransfer(transaction))
                .hasMessage(errorMessage);
    }

    @Test
    void transferBetweenSameAccount() {
        //Given source account exists
        Long sourceAccountId = accountFacade.createAccount(new BigDecimal("1000.00"), USD);

        //and both source and target accounts are the same
        //When a transaction request is received
        //Then the balance of source account should remain the same
        //and the client of the API should receive an error
        Transaction transaction = Transaction.builder()
                .currency(USD)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(sourceAccountId)
                .amount(new BigDecimal("5000.00"))
                .build();

        String errorMessage = String.format(TRANSACTION_FAILED_TRANSFERS_ARE_NOT_ALLOWED_IN_THE_SAME_ACCOUNT, sourceAccountId, sourceAccountId);

        assertThatThrownBy(() -> transactionFacade.applyTransfer(transaction))
                .hasMessage(errorMessage);

    }

    @Test
    void oneOrMoreOfTheAccountsDoesNotExist() {
        //Given source or target account do not exist
        //When a transaction request is received
        //Then the balance of the existing account should remain the same
        //and the client of the API should receive an error
        Transaction transaction = Transaction.builder()
                .currency(USD)
                .sourceAccountId(6L)
                .targetAccountId(7L)
                .amount(new BigDecimal("5000.00"))
                .build();


        String errorMessage = String.format(TRANSACTION_FAILED_WITH_ERROR_ACCOUNT_NOT_FOUND, 6L, 7L, 6L);

        assertThatThrownBy(() -> transactionFacade.applyTransfer(transaction))
                .hasMessage(errorMessage);

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
