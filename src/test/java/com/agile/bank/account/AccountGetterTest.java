package com.agile.bank.account;

import com.agile.bank.account.adapter.persistance.AccountEntity;
import com.agile.bank.account.domain.Account;
import com.agile.bank.account.exception.AccountNotFoundException;
import com.agile.bank.account.port.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountGetterTest {

    private static final long ACCOUNT_ID = 1L;
    private static final Currency USD = Currency.getInstance("USD");
    private static final LocalDateTime ACCOUNT_CREATED_AT = LocalDateTime.of(2023,11,26,11,30);
    private static final BigDecimal ACCOUNT_BALANCE = new BigDecimal("30.00");

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountGetter accountGetter;

    @Test
    void shouldReturnExistingAccountSuccessfully() throws AccountNotFoundException {
        Account expectedAccount = Account.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        AccountEntity accountEntity = AccountEntity.builder()
                .id(ACCOUNT_ID)
                .currency(USD)
                .balance(ACCOUNT_BALANCE)
                .createdAt(ACCOUNT_CREATED_AT)
                .build();

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(accountEntity));
        Account account = accountGetter.getAccount(ACCOUNT_ID);

        assertThat(account).isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountGetter.getAccount(ACCOUNT_ID))
                .hasMessage("Account with id: 1 not found");
    }
}