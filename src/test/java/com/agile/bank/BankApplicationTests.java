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

}
