package com.agile.bank.transaction;

import com.agile.bank.account.AccountFacade;
import com.agile.bank.account.exception.AccountNotFoundException;
import com.agile.bank.account.exception.InsufficientBalanceException;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import com.agile.bank.transaction.adapter.persistance.TransactionEntity;
import com.agile.bank.transaction.domain.Transaction;
import com.agile.bank.transaction.port.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionApplierTest {

    private static final long TRANSACTION_ID = 1L;
    private static final long SOURCE_ACCOUNT_ID = 1L;
    private static final long TARGET_ACCOUNT_ID = 2L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final BigDecimal TRANSFER_AMOUNT = new BigDecimal("30.00");

    @Mock
    private AccountFacade accountFacade;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionApplier transactionApplier;

    @Test
    void shouldReturnExistingTransactionSuccessfully() throws AccountNotFoundException, InvalidRequestAmountException, InsufficientBalanceException {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        Transaction expectedTransaction = Transaction.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity transactionEntityToBeSaved = TransactionEntity.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        TransactionEntity transactionEntitySaved = TransactionEntity.builder()
                .id(TRANSACTION_ID)
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        when(transactionRepository.save(transactionEntityToBeSaved)).thenReturn(transactionEntitySaved);
        Transaction transaction = transactionApplier.apply(transactionToBeSaved);

        assertThat(transaction).isEqualTo(expectedTransaction);
        verify(accountFacade, times(1)).verifyAccountWithCurrencyExists(transactionToBeSaved.getSourceAccountId(), transactionToBeSaved.getCurrency());
        verify(accountFacade, times(1)).verifyAccountWithCurrencyExists(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getCurrency());
        verify(accountFacade, times(1)).debitAccount(transactionToBeSaved.getSourceAccountId(), transactionToBeSaved.getAmount());
        verify(accountFacade, times(1)).creditAccount(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenTransactionAppliesInSameAccount() {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(SOURCE_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        assertThatThrownBy(() ->  transactionApplier.apply(transactionToBeSaved))
                .hasMessage("Transaction failed, source account : 1 and target account : 1, with error: Transfers are not allowed in the same account");

    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForRequestedCurrency() {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        doThrow(AccountNotFoundException.of(SOURCE_ACCOUNT_ID)).when(accountFacade)
                .verifyAccountWithCurrencyExists(transactionToBeSaved.getSourceAccountId(), transactionToBeSaved.getCurrency());

        assertThatThrownBy(() ->  transactionApplier.apply(transactionToBeSaved))
                .hasMessage("Transaction failed, source account : 1 and target account : 2, with currency: USD, with error: Account with id: 1 not found");

        verify(accountFacade, never()).verifyAccountWithCurrencyExists(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getCurrency());
        verify(accountFacade, never()).debitAccount(transactionToBeSaved.getSourceAccountId(), transactionToBeSaved.getAmount());
        verify(accountFacade, never()).creditAccount(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenAccountHasInsufficientBalance() {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        doThrow(InsufficientBalanceException.of(SOURCE_ACCOUNT_ID)).when(accountFacade)
                .debitAccount(transactionToBeSaved.getSourceAccountId(), transactionToBeSaved.getAmount());

        assertThatThrownBy(() ->  transactionApplier.apply(transactionToBeSaved))
                .hasMessage("Transaction failed, source account : 1 and target account : 2, with currency: USD, with error: Account with id: 1 has insufficient balance for the transfer request");

        verify(accountFacade, never()).creditAccount(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenTransactionRequestHasInvalidAmount() {
        Transaction transactionToBeSaved = Transaction.builder()
                .currency(USD)
                .sourceAccountId(SOURCE_ACCOUNT_ID)
                .targetAccountId(TARGET_ACCOUNT_ID)
                .amount(TRANSFER_AMOUNT)
                .build();

        doThrow(InvalidRequestAmountException.ofCredit(TARGET_ACCOUNT_ID,"Amount should be positive and with two decimals")).when(accountFacade)
                .creditAccount(transactionToBeSaved.getTargetAccountId(), transactionToBeSaved.getAmount());

        assertThatThrownBy(() ->  transactionApplier.apply(transactionToBeSaved))
                .hasMessage("Transaction failed, source account : 1 and target account : 2, with currency: USD, with error: Credit request for account with id: 2 failed, with error: Amount should be positive and with two decimals");

    }
}