package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.InvalidRequestAmountException;
import com.agile.bank.account.port.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCreatorTest {

    private static final long DB_ID = 1L;
    @Mock
    private Clock clock;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountCreator accountCreator;


    @Test
    void shouldCreateAccountSuccessfully() throws InvalidRequestAmountException {
        Instant instant = Instant.parse("2023-11-26T16:00:45.00Z");
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        Currency usd = Currency.getInstance("USD");
        BigDecimal depositAmount = new BigDecimal("10.00");

        AccountEntity createdAccountEntity = AccountEntity.builder()
                .currency(usd)
                .balance(depositAmount)
                .createdAt(dateTime)
                .build();

        AccountEntity savedAccountEntity = AccountEntity.builder()
                .id(DB_ID)
                .currency(usd)
                .balance(depositAmount)
                .createdAt(dateTime)
                .build();

        when(accountRepository.save(createdAccountEntity)).thenReturn(savedAccountEntity);

        Account account = accountCreator.createAccount(depositAmount, usd);

        Account expectedAccount = Account.builder()
                .id(DB_ID)
                .currency(usd)
                .balance(depositAmount)
                .createdAt(dateTime)
                .build();

        assertThat(account).isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionForAnInvalidDepositAmount() {
        Currency usd = Currency.getInstance("USD");
        BigDecimal depositAmount = new BigDecimal("10.0");

        assertThatThrownBy(() -> accountCreator.createAccount(depositAmount, usd))
                .hasMessage("New account request failed, with error: Amount should be positive and with two decimals");
    }
}