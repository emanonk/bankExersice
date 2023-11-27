package com.agile.bank.account;

import com.agile.bank.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountFacadeImplTest {

    private static final long ACCOUNT_ID = 1L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final LocalDateTime ACCOUNT_CREATED_AT = LocalDateTime.of(2023,11,26,11,30);
    private static final BigDecimal ACCOUNT_BALANCE = new BigDecimal("30.00");
    private static final BigDecimal REQUEST_AMOUNT = new BigDecimal("20.00");

    @Mock
    private AccountCreator accountCreator;
    @Mock
    private AccountGetter accountGetter;
    @Mock
    private AccountDebtor accountDebtor;
    @Mock
    private AccountCreditor accountCreditor;
    @InjectMocks
    private AccountFacadeImpl accountFacade;

    @Test
    void createAccount() {
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountCreator.createAccount(ACCOUNT_BALANCE, USD)).thenReturn(account);

        Long accountId = accountFacade.createAccount(ACCOUNT_BALANCE, USD);

        assertThat(accountId).isEqualTo(ACCOUNT_ID);
    }

    @Test
    void getAccount() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        Account account = accountFacade.getAccount(ACCOUNT_ID);

        assertThat(account).isEqualTo(expectedAccount);
    }

    @Test
    void verifyAccountWithCurrencyExists() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        accountFacade.verifyAccountWithCurrencyExists(ACCOUNT_ID, USD);
    }

    @Test
    void verifyAccountWithCurrencyNotFound() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(EUR)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        assertThatThrownBy(() -> accountFacade.verifyAccountWithCurrencyExists(ACCOUNT_ID, USD))
                .hasMessage("Account with id: 1 not found");

    }

    @Test
    void debitAccount() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        accountFacade.debitAccount(ACCOUNT_ID, REQUEST_AMOUNT);

        verify(accountDebtor, times(1)).debit(expectedAccount, REQUEST_AMOUNT);
    }

    @Test
    void creditAccount() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        accountFacade.creditAccount(ACCOUNT_ID, REQUEST_AMOUNT);

        verify(accountCreditor, times(1)).credit(expectedAccount, REQUEST_AMOUNT);
    }

    @Test
    void getAccountBalance() {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountGetter.getAccount(ACCOUNT_ID)).thenReturn(expectedAccount);

        BigDecimal accountBalance = accountFacade.getAccountBalance(ACCOUNT_ID);
        assertThat(accountBalance).isEqualTo(ACCOUNT_BALANCE);
    }
}