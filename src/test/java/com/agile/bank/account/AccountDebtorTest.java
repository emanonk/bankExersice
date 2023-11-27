package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.InsufficientBalanceException;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import com.agile.bank.account.port.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountDebtorTest {

    private static final long DB_ID = 1L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final LocalDateTime ACCOUNT_CREATED_AT = LocalDateTime.of(2023,11,26,11,10);

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountDebtor accountDebtor;

    @Captor
    ArgumentCaptor<AccountEntity> accountEntityArgumentCaptor;
    @Test
    void shouldDebitAccountSuccessfully() throws InvalidRequestAmountException, InsufficientBalanceException {
        BigDecimal accountBalance = new BigDecimal("30.00");
        BigDecimal debitAmount = new BigDecimal("20.00");
        BigDecimal newAccountBalance = new BigDecimal("10.00");
        Account account = Account.builder()
                .id(DB_ID)
                .currency(USD)
                .balance(accountBalance)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        accountDebtor.debit(account, debitAmount);

        verify(accountRepository).save(accountEntityArgumentCaptor.capture());
        AccountEntity savedAccountEntity = accountEntityArgumentCaptor.getValue();

        AccountEntity expectedAccountEntity = AccountEntity.builder()
                .id(DB_ID)
                .currency(USD)
                .balance(newAccountBalance)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        assertThat(savedAccountEntity).isEqualTo(expectedAccountEntity);
    }

    @Test
    void shouldThrowExceptionForInsufficientAccountBalance() {
        BigDecimal accountBalance = new BigDecimal("30.00");
        BigDecimal debitAmount = new BigDecimal("50.00");


        Account account = Account.builder()
                .id(DB_ID)
                .currency(USD)
                .balance(accountBalance)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        assertThatThrownBy(() -> accountDebtor.debit(account, debitAmount))
                .hasMessage("Account with id: 1 has insufficient balance for the transfer request");
    }

    @Test
    void shouldThrowExceptionForAnInvalidDebitAmount() {
        BigDecimal accountBalance = new BigDecimal("10.00");
        Account account = Account.builder()
                .id(DB_ID)
                .currency(USD)
                .balance(accountBalance)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        BigDecimal debitAmount = new BigDecimal("-50.0");

        assertThatThrownBy(() -> accountDebtor.debit(account, debitAmount))
                .hasMessage("Debit request for account with id: 1 failed, with error: Amount should be positive and with two decimals");
    }


}